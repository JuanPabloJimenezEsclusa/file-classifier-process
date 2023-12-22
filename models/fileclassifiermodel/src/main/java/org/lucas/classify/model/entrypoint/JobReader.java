package org.lucas.classify.model.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DocumentClassificationJobFilter;
import software.amazon.awssdk.services.comprehend.model.ListDocumentClassificationJobsRequest;

import static org.lucas.classify.model.entrypoint.Config.*;

// https://aws.amazon.com/es/comprehend/pricing/
class JobReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobReader.class);

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(getValue(SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = ListDocumentClassificationJobsRequest.builder()
        .filter(DocumentClassificationJobFilter.builder()
          .jobName(getValue(JOB_FULL_NAME)).build())
        .build();

      var response = comprehendClient.listDocumentClassificationJobs(request);
      response.documentClassificationJobPropertiesList().forEach(classificationJobProperties ->
        LOGGER.info("Document Classification Job Properties: {}", classificationJobProperties.message()));

    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
