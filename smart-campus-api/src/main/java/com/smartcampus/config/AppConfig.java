package com.smartcampus.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;

/**
 * JAX-RS Application configuration class that registers all resource classes.
 * Defines the base API path as /api/v1.
 */
@ApplicationPath("/api/v1")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        // Register Jackson feature for JSON support
        register(JacksonFeature.class);
        
        // Register all resource classes here
        register(DiscoveryResource.class);
        register(RoomResource.class);
        
        // Enable auto-discovery of resources in this package
        packages("com.smartcampus.resource");
    }
}
