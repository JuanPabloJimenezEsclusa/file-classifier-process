package org.lucas.classify.model.classifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;

import static org.lucas.classify.model.classifier.Config.*;

// https://aws.amazon.com/es/comprehend/pricing/
// https://docs.aws.amazon.com/comprehend/latest/dg/prep-class-data-format.html
// https://docs.aws.amazon.com/comprehend/latest/dg/guidelines-and-limits.html#limits-class-general
class DocumentClassifierCreator {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentClassifierCreator.class);

  private DocumentClassifierCreator() { }

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    // no permite LanguageCode.ES sí se utiliza DocumentClassifierDocumentTypeFormat.SEMI_STRUCTURED_DOCUMENT
    var languageCode = LanguageCode.EN;

    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(getValue(SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = CreateDocumentClassifierRequest.builder()
        .documentClassifierName(getValue(DOCUMENT_CLASSIFIER_NAME))
        .dataAccessRoleArn(getValue(DATA_ACCESS_ROLE))
        .inputDataConfig(DocumentClassifierInputDataConfig.builder()
          .s3Uri(getValue(TRAINING_DATA_S3URI) + getValue(TRAINING_CONFIG_FILE))
          .dataFormat(DocumentClassifierDataFormat.COMPREHEND_CSV)
          .documents(builder -> builder.s3Uri(getValue(TRAINING_DATA_S3URI)))
          .documentType(DocumentClassifierDocumentTypeFormat.SEMI_STRUCTURED_DOCUMENT)
          .documentReaderConfig(builder -> builder
            .documentReadAction(DocumentReadAction.TEXTRACT_DETECT_DOCUMENT_TEXT)
            .documentReadMode(DocumentReadMode.FORCE_DOCUMENT_READ_ACTION))
          .build())
        .outputDataConfig(DocumentClassifierOutputDataConfig.builder().s3Uri(getValue(OUTPUT_DATA_S3URI)).build())
        .languageCode(languageCode)
        .mode(DocumentClassifierMode.MULTI_CLASS)
        .tags(Tag.builder().key(getValue(ENVIRONMENT_KEY)).value(getValue(ENVIRONMENT_VALUE)).build())
        .versionName(getValue(DOCUMENT_CLASSIFIER_VERSION))
        .build();

      var response = comprehendClient.createDocumentClassifier(request);
      LOGGER.info("Document Classifier ARN: {}", response.documentClassifierArn());
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
