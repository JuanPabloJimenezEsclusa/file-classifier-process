package org.lucas.classify.model.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DeleteEndpointRequest;

// https://docs.aws.amazon.com/comprehend/latest/dg/guidelines-and-limits.html#limits-class-general
class EndpointCleanup {
  private static final Logger LOGGER = LoggerFactory.getLogger(EndpointCleanup.class);

  private EndpointCleanup() {}

  public static void main(String[] args) {
    apply();
  }

  private static void apply() {
    try (var comprehendClient = ComprehendClient.builder()
      .region(Region.of(Config.getValue(Config.SERVICE_REGION)))
      .credentialsProvider(ProfileCredentialsProvider.create())
      .build()) {

      var request = DeleteEndpointRequest.builder()
        .endpointArn(Config.getValue(Config.ENDPOINT_ARN))
        .build();

      var response = comprehendClient.deleteEndpoint(request);
      LOGGER.info("Endpoint Arn: {}", response.sdkHttpResponse());
    } catch (ComprehendException e) {
      LOGGER.error("Exception: {}", e.getMessage());
    }
  }
}
