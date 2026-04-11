package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.config.AppConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    private static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void main(String[] args) throws IOException {
        try {
            // Create and start the embedded Grizzly HTTP server
            ResourceConfig config = ResourceConfig.forApplication(new AppConfig());
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(BASE_URI), 
                    config
            );
            
            // Block the current thread until server stops
            server.start();
            
            System.out.println("Smart Campus API started successfully!");
            System.out.println("Base URI: " + BASE_URI);
            System.out.println("API Endpoint: " + BASE_URI + "api/v1");
            System.out.println("Press Ctrl+C to stop the server.");
            
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Server interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
