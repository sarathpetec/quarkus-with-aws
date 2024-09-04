package org.acme.aws;

import software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryClient;
import software.amazon.awssdk.services.servicediscovery.model.ListServicesRequest;
import software.amazon.awssdk.services.servicediscovery.model.ListServicesResponse;

public class ServiceDiscoveryHelper {

    private final ServiceDiscoveryClient serviceDiscoveryClient;

    public ServiceDiscoveryHelper(ServiceDiscoveryClient serviceDiscoveryClient) {
        this.serviceDiscoveryClient = serviceDiscoveryClient;
    }

    public String getServiceIdByName(String serviceName) {
        ListServicesRequest listServicesRequest = ListServicesRequest.builder().build();
        ListServicesResponse listServicesResponse = serviceDiscoveryClient.listServices(listServicesRequest);

        return listServicesResponse.services().stream()
                .filter(service -> service.name().equals(serviceName))
                .findFirst()
                .map(service -> service.id())
                .orElseThrow(() -> new RuntimeException("Service not found: " + serviceName));
    }
}
