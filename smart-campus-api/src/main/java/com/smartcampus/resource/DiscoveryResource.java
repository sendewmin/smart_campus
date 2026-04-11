package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * JAX-RS resource for API discovery and information.
 * Provides metadata about the Smart Campus API.
 */
@Path("/")
public class DiscoveryResource {

    /**
     * GET method that returns API discovery information.
     *
     * @return a Map containing API metadata and available endpoints
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDiscovery() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("version", "1.0");
        response.put("description", "Smart Campus API");
        response.put("contact", "admin@smartcampus.com");
        
        // Endpoints map
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("rooms", "/api/v1/rooms");
        endpoints.put("sensors", "/api/v1/sensors");
        
        response.put("endpoints", endpoints);
        
        return response;
    }
}
