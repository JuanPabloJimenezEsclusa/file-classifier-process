package org.lucas.classify.document.config;

public enum EnvConfig {
  LOCAL_FILE_PATH("/tmp/file.tmp"),
  BUCKET_FILE("file-classifier-process-s3-02"),
  ENDPOINT_ARN("arn:aws:comprehend:us-west-2:*******:document-classifier-endpoint/files-process-endpoint");

  private final String defaultValue;

  EnvConfig(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public static String getValue(EnvConfig config) {
    var envValue = System.getenv(config.name());
    return envValue != null ? envValue : config.defaultValue;
  }
}
