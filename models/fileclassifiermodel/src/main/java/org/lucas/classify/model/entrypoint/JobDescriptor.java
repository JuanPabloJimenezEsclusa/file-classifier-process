package org.lucas.classify.model.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DescribeDocumentClassificationJobRequest;

import static org.lucas.classify.model.entrypoint.Config.*;

// https://aws.amazon.com/es/comprehend/pricing/
class JobDescriptor {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobDescriptor.class);

  private JobDescriptor() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(getValue(SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = DescribeDocumentClassificationJobRequest.builder()
        .jobId(getValue(JOB_ID))
        .build();

      var response = comprehendClient.describeDocumentClassificationJob(request);
      LOGGER.info("Document Classification Job Properties: {}", response.documentClassificationJobProperties());
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
