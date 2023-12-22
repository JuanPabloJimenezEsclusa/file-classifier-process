package org.lucas.classify.model.classifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DeleteDocumentClassifierRequest;

import static org.lucas.classify.model.classifier.Config.*;

class DocumentClassifierCleaner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentClassifierCleaner.class);

  private DocumentClassifierCleaner() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(getValue(SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = DeleteDocumentClassifierRequest.builder()
        .documentClassifierArn(getValue(CLASSIFIER_ID))
        .build();

      var response = comprehendClient.deleteDocumentClassifier(request);
      LOGGER.info("Document Classifier Metadata: {}", response.responseMetadata());
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
