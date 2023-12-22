package org.lucas.classify.model.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;

import static org.lucas.classify.model.entrypoint.Config.*;

// https://docs.aws.amazon.com/comprehend/latest/dg/idp-inputs-sync.html
// https://docs.aws.amazon.com/comprehend/latest/dg/idp-images-bp.html
class JobCreator {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobCreator.class);

  private JobCreator() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(getValue(SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = StartDocumentClassificationJobRequest.builder()
        .jobName(getValue(JOB_NAME))
        .documentClassifierArn(getValue(CLASSIFIER_ID))
        .dataAccessRoleArn(getValue(DATA_ACCESS_ROLE))
        .inputDataConfig(InputDataConfig.builder()
          .s3Uri(getValue(TEST_DATA_INPUT_S3URI))
          .inputFormat(InputFormat.ONE_DOC_PER_FILE)
          .documentReaderConfig(builder -> builder
            .documentReadAction(DocumentReadAction.TEXTRACT_DETECT_DOCUMENT_TEXT)
            .documentReadMode(DocumentReadMode.FORCE_DOCUMENT_READ_ACTION))
          .build())
        .outputDataConfig(OutputDataConfig.builder().s3Uri(getValue(TEST_DATA_OUTPUT_S3URI)).build())
        .tags(Tag.builder().key(getValue(ENVIRONMENT_KEY)).value(getValue(ENVIRONMENT_VALUE)).build())
        .build();

      var response = comprehendClient.startDocumentClassificationJob(request);
      LOGGER.info("Job: (ID: {} - Status: {} - Arn: {})", response.jobId(), response.jobStatusAsString(), response.jobArn());
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
