package org.lucas.classify.model.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.CreateEndpointRequest;
import software.amazon.awssdk.services.comprehend.model.Tag;

// https://docs.aws.amazon.com/comprehend/latest/dg/guidelines-and-limits.html#limits-class-general
class EndpointCreator {
  private static final Logger LOGGER = LoggerFactory.getLogger(EndpointCreator.class);

  private EndpointCreator() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(Config.getValue(Config.SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = CreateEndpointRequest.builder()
        .endpointName(Config.getValue(Config.ENDPOINT_NAME))
        .desiredInferenceUnits(Integer.parseInt(Config.getValue(Config.ENDPOINT_INFERENCE_UNITS)))
        .dataAccessRoleArn(Config.getValue(Config.DATA_ACCESS_ROLE))
        .modelArn(Config.getValue(Config.CLASSIFIER_ID))
        .tags(Tag.builder().key(Config.getValue(Config.ENVIRONMENT_KEY)).value(Config.getValue(Config.ENVIRONMENT_VALUE)).build())
        .build();

      var response = comprehendClient.createEndpoint(request);
      LOGGER.info("Endpoint Arn: {}", response.responseMetadata());
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
