package org.lucas.classify.document.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.lucas.classify.document.config.DependencyFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.lucas.classify.document.config.EnvConfig.*;

public record ComprehendProxy(S3Client s3Client,
                              ComprehendClient comprehendClient) {
  public ComprehendProxy() {
    this(DependencyFactory.s3Client(),
      DependencyFactory.comprehendClient());
  }

  public List<DocumentClass> classify(S3EventNotification.S3ObjectEntity object, Context context) {
    String errorMessage = "Was not able to find object";
    String localFilePath = getValue(LOCAL_FILE_PATH);
    var s3Path = Path.of(localFilePath);
    deleteLocalPath(localFilePath, context);

    try {
      String bucket = getValue(BUCKET_FILE);
      String key = object.getKey();
      context.getLogger().log("bucket: (" + bucket + ") - key: (" + key + ")");

      var s3Object = s3Client.getObject(GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build(), Path.of(localFilePath));
      if (s3Object != null) {
        var request = ClassifyDocumentRequest.builder()
          .endpointArn(getValue(ENDPOINT_ARN))
          .documentReaderConfig(DocumentReaderConfig.builder()
            .documentReadMode(DocumentReadMode.FORCE_DOCUMENT_READ_ACTION)
            .documentReadAction(DocumentReadAction.TEXTRACT_ANALYZE_DOCUMENT)
            .build())
          .bytes(loadToBytes(s3Path.toFile()))
          .build();
        return comprehendClient.classifyDocument(request).classes();
      }

      context.getLogger().log("Was not able to find object");
    } catch (S3Exception | ComprehendException exception) {
      context.getLogger().log(exception.getMessage());
      errorMessage = exception.getMessage();
    }
    return List.of(DocumentClass.builder().name(errorMessage).page(0).score(1.0f).build());
  }

  private static void deleteLocalPath(String localFilePath, Context context) {
    try {
      Files.deleteIfExists(Path.of(localFilePath));
    } catch (IOException e) {
      context.getLogger().log(e.getMessage());
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
