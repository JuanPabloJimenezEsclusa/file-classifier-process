package org.lucas.classify.document.service;

import com.amazonaws.services.lambda.runtime.Context;
import org.lucas.classify.document.config.EnvConfig;
import org.lucas.classify.document.config.DependencyFactory;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.TextractException;

import java.util.List;
import java.util.stream.Collectors;

import static org.lucas.classify.document.config.EnvConfig.getValue;

public record TextractProxy(TextractClient textractClient) {

  public TextractProxy() {
    this(DependencyFactory.textractClient());
  }

  public List<String> detectText(String filename, Context context) {
    try {
      var request = DetectDocumentTextRequest.builder()
        .document(b -> b.s3Object(s -> s.bucket(getValue(EnvConfig.BUCKET_FILE)).name(filename)))
        .build();
      var textBlock = textractClient.detectDocumentText(request).blocks();
      context.getLogger().log("Text block: " + textBlock.toString());

      return processText(textBlock);
    } catch (TextractException exception) {
      context.getLogger().log(exception.getMessage());
      return List.of(exception.getMessage());
    }
  }

  private static List<String> processText(List<Block> blocks) {
    return blocks.stream()
      .filter(block -> isLineType(block.blockType()))
      .map(Block::text)
      .collect(Collectors.toList());
  }

  private static boolean isLineType(BlockType blockType) {
    return BlockType.LINE.equals(blockType);
  }
}
