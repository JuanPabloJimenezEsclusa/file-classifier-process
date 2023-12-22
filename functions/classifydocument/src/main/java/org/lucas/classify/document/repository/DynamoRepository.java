package org.lucas.classify.document.repository;

import com.amazonaws.services.lambda.runtime.Context;
import org.lucas.classify.document.config.DependencyFactory;
import software.amazon.awssdk.services.comprehend.model.DocumentClass;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record DynamoRepository(DynamoDbClient dynamoDbClient) {

  public DynamoRepository() {
    this(DependencyFactory.dynamoDbClient());
  }

  public void putDBItem(String filename,
                        List<DocumentClass> classifyDocument,
                        List<String> processResult,
                        long start,
                        Context context) {
    try {
      var classifyAttributeValues = classifyDocument.stream()
        .collect(Collectors.toMap(DocumentClass::name, documentClass ->
          AttributeValue.builder().n(Float.toString(documentClass.score())).build()));

      var resultAttributeValues = processResult.stream()
        .map(result ->  AttributeValue.builder().s(result).build())
        .collect(Collectors.toList());

      var putItemRequest = PutItemRequest.builder()
        .tableName("process_result")
        .item(Map.of(
          "UserId", AttributeValue.builder().s(UUID.randomUUID().toString()).build(),
          "CreatedDate", AttributeValue.builder().s(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)).build(),
          "FileName", AttributeValue.builder().s(filename).build(),
          "ClassifyDocument", AttributeValue.builder().m(classifyAttributeValues).build(),
          "ContentDocument", AttributeValue.builder().l(resultAttributeValues).build(),
          "ElapsedTime", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() - start)).build()
        ))
        .build();

      context.getLogger().log("Put Item Request: " + putItemRequest.toString());
      dynamoDbClient.putItem(putItemRequest);

    } catch (DynamoDbException exception){
      context.getLogger().log("ERROR! Put Item Request: " + exception.getMessage());
    }
  }
}
