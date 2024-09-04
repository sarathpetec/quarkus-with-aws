package org.acme.config;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.acme.aws.ServiceRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class ApplicationInitialization {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Inject
    ServiceRegistrar serviceRegistrar;

    void onStart(@Observes StartupEvent event) throws Throwable {
        VertxContextSupport.subscribeAndAwait(() -> {
            return Uni.createFrom().item(() -> {
                try {
                    serviceRegistrar.registerService();
                    logger.info("FeedService successfully registered with AWS Cloud Map.");
                } catch (Exception e) {
                    logger.error("Failed to register FeedService with AWS Cloud Map", e);
                }
                return null;
            });
        });
    }

    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping...");
    }
}
