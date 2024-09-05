package org.acme.aws;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryClient;
import software.amazon.awssdk.services.servicediscovery.model.RegisterInstanceRequest;
import software.amazon.awssdk.services.servicediscovery.model.RegisterInstanceResponse;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@ApplicationScoped
public class ServiceRegistrar {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ServiceDiscoveryClient serviceDiscoveryClient;

    //private final String SERVICE_ID = getServiceID();
    private String SERVICE_ID;

    @ConfigProperty(name = "quarkus.profile")
    String profile;

    @Inject
    public ServiceRegistrar(ServiceDiscoveryClient serviceDiscoveryClient) {
        logger.info("ServiceRegistrar initializing");
        this.serviceDiscoveryClient = serviceDiscoveryClient;
    }

    @PostConstruct
    public void init() {
        logger.info("init called in ServiceRegistrar");
    }


    public void registerService() {
        if ("local".equals(profile)) {
            logger.info("Skipping AWS Cloud Map registration in local profile");
            return;
        }
        SERVICE_ID = getServiceID();
        //SERVICE_ID = "TEST";

        logger.info("profile in registerService:{} ", profile);
        String instanceIp = InstanceMetadata.getInstanceIp(); // Get the IP dynamically

        RegisterInstanceRequest request = RegisterInstanceRequest.builder()
                .serviceId(SERVICE_ID)
                .instanceId("test-instance-" + instanceIp) // Use a unique ID, potentially based on IP or other identifier
                .attributes(Map.of(
                        "AWS_INSTANCE_IPV4", instanceIp, // Use the dynamically retrieved IP
                        "AWS_INSTANCE_PORT", "8091"))
                .build();

        RegisterInstanceResponse response = serviceDiscoveryClient.registerInstance(request);

        logger.info("Registered service with instance ID: " + response.operationId());
    }

    private String getServiceID(){
        ServiceDiscoveryHelper helper = new ServiceDiscoveryHelper(serviceDiscoveryClient);
        return helper.getServiceIdByName("test");

    }
}

