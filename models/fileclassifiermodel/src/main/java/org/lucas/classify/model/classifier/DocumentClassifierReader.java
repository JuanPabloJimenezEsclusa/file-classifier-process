package org.lucas.classify.model.classifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DocumentClassifierFilter;
import software.amazon.awssdk.services.comprehend.model.ListDocumentClassifiersRequest;

import static org.lucas.classify.model.classifier.Config.*;

class DocumentClassifierReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentClassifierReader.class);

  private DocumentClassifierReader() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(getValue(SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = ListDocumentClassifiersRequest.builder()
        .filter(DocumentClassifierFilter.builder()
          .documentClassifierName(getValue(DOCUMENT_CLASSIFIER_NAME))
          .build())
        .build();

      var response = comprehendClient.listDocumentClassifiersPaginator(request);
      response.forEach(classifier ->
        LOGGER.info("Document Classifier Properties: {}", classifier.documentClassifierPropertiesList()));
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
