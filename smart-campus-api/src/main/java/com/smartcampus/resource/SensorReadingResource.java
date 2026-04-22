package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * JAX-RS sub-resource for managing sensor readings for a specific sensor.
 * Provides endpoints for CRUD operations on sensor readings.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;
    private final DataStore dataStore = DataStore.getInstance();

    /**
     * Constructor that takes a sensor id.
     *
     * @param sensorId the id of the sensor whose readings are managed
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * GET method that returns all readings for this sensor.
     *
     * @return a list of all sensor readings for this sensor
     */
    @GET
    public List<SensorReading> getAllReadings() {
        return dataStore.getSensorReadings(sensorId);
    }

    /**
     * POST method that creates a new sensor reading.
     * Generates a UUID for the reading id and sets the current timestamp.
     * Also updates the parent Sensor's currentValue field with the new reading's value.
     *
     * @param reading the sensor reading object to create (id and timestamp will be overwritten)
     * @return Response with 201 Created status and Location header
     * @throws NotFoundException if the sensor is not found
     */
    @POST
    public Response createReading(SensorReading reading) {
        // Validate that the sensor exists
        Sensor sensor = dataStore.getSensor(sensorId);
        
        if (sensor == null) {
            throw new NotFoundException("Sensor with id " + sensorId + " not found");
        }
        
        // Check if sensor is under maintenance
        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is currently under maintenance and cannot accept readings");
        }
        
        // Generate UUID for the reading id
        String readingId = UUID.randomUUID().toString();
        reading.setId(readingId);
        
        // Set current timestamp (in milliseconds)
        reading.setTimestamp(System.currentTimeMillis());
        
        // Add reading to DataStore
        dataStore.addSensorReading(sensorId, reading);
        
        // Update the parent Sensor's currentValue with the new reading's value
        sensor.setCurrentValue(reading.getValue());
        
        // Create Location header
        URI location = UriBuilder.fromResource(SensorResource.class)
                .path(SensorResource.class, "getSensor")
                .path(SensorReadingResource.class, "getAllReadings")
                .build(sensorId);
        
        return Response.created(location)
                .entity(reading)
                .build();
    }
}
