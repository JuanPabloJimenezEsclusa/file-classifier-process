package org.lucas.classify.model.classifier;

enum Config {
  // global
  SERVICE_REGION("us-west-2"),
  DATA_ACCESS_ROLE("arn:aws:iam::*******:role/service-role/AmazonComprehendServiceRole-file-classifier-process"),
  ENVIRONMENT_KEY("ENVIRONMENT"),
  ENVIRONMENT_VALUE("DEV"),

  // model
  CLASSIFIER_ID("arn:aws:comprehend:us-west-2:*******:document-classifier/files-process-classifier/version/v1"),
  TRAINING_DATA_S3URI("s3://file-classifier-process-s3-01/process/trainingData"),
  TRAINING_CONFIG_FILE("/model-training.csv"),
  OUTPUT_DATA_S3URI("s3://file-classifier-process-s3-01/process/outputData"),
  DOCUMENT_CLASSIFIER_NAME("files-process-model"),
  DOCUMENT_CLASSIFIER_VERSION("v1");

  private final String defaultValue;

  Config(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  static String getValue(Config config) {
    var envValue = System.getenv(config.name());
    return envValue != null ? envValue : config.defaultValue;
  }
}
