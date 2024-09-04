package org.acme.aws;

import io.quarkus.arc.DefaultBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryClient;

@ApplicationScoped
public class AwsClientProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostConstruct
    void init() {
        logger.info("AwsClientProducer initialized.");
    }

    @Produces
    @DefaultBean
    public ServiceDiscoveryClient serviceDiscoveryClient(@ConfigProperty(name = "quarkus.profile") String profile) {
        logger.info("profile: "+profile);
        if ("local".equals(profile)) {
            logger.info("Local profile and setting Mockito Object......");
            return Mockito.mock(ServiceDiscoveryClient.class);
        } else {
            return ServiceDiscoveryClient.builder()
                    .region(Region.of("eu-west-1"))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
    }
}
