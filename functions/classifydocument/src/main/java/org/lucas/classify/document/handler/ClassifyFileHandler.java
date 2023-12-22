package org.lucas.classify.document.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.lucas.classify.document.repository.DynamoRepository;
import org.lucas.classify.document.service.ComprehendProxy;
import org.lucas.classify.document.service.TextractProxy;

public class ClassifyFileHandler implements RequestHandler<S3Event, String> {
  private final ComprehendProxy comprehendProxy = new ComprehendProxy();
  private final TextractProxy textractProxy = new TextractProxy();
  private final DynamoRepository dynamoRepository = new DynamoRepository();

  @Override
  public String handleRequest(S3Event s3Event, Context context) {
    s3Event.getRecords().stream()
      .filter(item -> !isProcessFolder(item))
      .forEach(item -> process(context, item));
    return "Classify Document Finish";
  }

  private boolean isProcessFolder(S3EventNotification.S3EventNotificationRecord item) {
    return "process/".equalsIgnoreCase(item.getS3().getObject().getKey());
  }

  private void process(Context context, S3EventNotification.S3EventNotificationRecord item) {
    context.getLogger().log("Record event name: " + item.getEventName());
    long start = System.currentTimeMillis();

    var s3Object = item.getS3().getObject();
    context.getLogger().log("S3 object: " + s3Object.toString());

    var documentResponse = comprehendProxy.classify(s3Object, context);
    context.getLogger().log("Document response: " + documentResponse);

    var processResult = textractProxy.detectText(s3Object.getKey(), context);
    context.getLogger().log("Process result: " + processResult.toString());

    dynamoRepository.putDBItem(s3Object.getKey(), documentResponse, processResult, start, context);
  }
}
