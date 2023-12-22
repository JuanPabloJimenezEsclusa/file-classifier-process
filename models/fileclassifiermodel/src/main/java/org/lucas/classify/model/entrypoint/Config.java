package org.lucas.classify.model.entrypoint;

enum Config {
  // global
  SERVICE_REGION("us-west-2"),
  DATA_ACCESS_ROLE("arn:aws:iam::*****:role/service-role/AmazonComprehendServiceRole-file-classifier-process"),
  ENVIRONMENT_KEY("ENVIRONMENT"),
  ENVIRONMENT_VALUE("DEV"),

  // model
  CLASSIFIER_ID("arn:aws:comprehend:us-west-2:*****:document-classifier/files-process-classifier/version/v1"),

  // entrypoint
  TEST_DATA_INPUT_S3URI("s3://file-classifier-process-s3-01/process/productionData"),
  TEST_DATA_OUTPUT_S3URI("s3://file-classifier-process-s3-01/process/productionOutputData"),
  JOB_NAME("files-process-entrypoint"),
  JOB_ID("77da858d8686861d7145f465503c85fa"),
  JOB_FULL_NAME("arn:aws:comprehend:us-west-2:*****:document-classification-job/77da858d8686861d7145f465503c85fa"),

  // endpoint
  ENDPOINT_NAME("files-process-endpoint"),
  ENDPOINT_INFERENCE_UNITS("5"),
  ENDPOINT_ARN("arn:aws:comprehend:us-west-2:*****:document-classifier-endpoint/files-process-endpoint");

  private final String defaultValue;

  Config(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  static String getValue(Config config) {
    var envValue = System.getenv(config.name());
    return envValue != null ? envValue : config.defaultValue;
  }
}
