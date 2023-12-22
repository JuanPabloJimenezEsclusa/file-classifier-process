package org.lucas.classify.model.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ClassifyDocument {
  private final static Logger LOGGER = LoggerFactory.getLogger(ClassifyDocument.class);

  private ClassifyDocument() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var s3Client = S3Client.builder()
          .region(Region.of(Config.getValue(Config.SERVICE_REGION)))
          .credentialsProvider(ProfileCredentialsProvider.create())
          .build();
         var comprehendClient = ComprehendClient.builder()
          .region(Region.of(Config.getValue(Config.SERVICE_REGION)))
          .credentialsProvider(ProfileCredentialsProvider.create())
          .build()) {

      String localFilePath = "/tmp/file.tmp";
      Files.deleteIfExists(Path.of(localFilePath));

      var s3Path = Path.of(localFilePath);
      var s3Object = s3Client.getObject(GetObjectRequest.builder()
        .bucket("file-classifier-process-s3-01")
        .key("process/productionData/file-01.pdf")
        .build(), Path.of(localFilePath));
      if (s3Object == null) {
        LOGGER.info("Was not able to find object, bailing");
        throw new IllegalArgumentException("Object not found");
      }

      var request = ClassifyDocumentRequest.builder()
        .endpointArn(Config.getValue(Config.ENDPOINT_ARN))
        .documentReaderConfig(DocumentReaderConfig.builder()
          .documentReadMode(DocumentReadMode.FORCE_DOCUMENT_READ_ACTION)
          .documentReadAction(DocumentReadAction.TEXTRACT_ANALYZE_DOCUMENT)
          .build())
        .bytes(loadToBytes(s3Path.toFile()))
        .build();

      var response = comprehendClient.classifyDocument(request);
      LOGGER.info("Classify: {}", response.toString());
    } catch (ComprehendException | IOException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }

  private static SdkBytes loadToBytes(File file) {
    try (var inputStream = new FileInputStream(file)) {
      return SdkBytes.fromInputStream(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
