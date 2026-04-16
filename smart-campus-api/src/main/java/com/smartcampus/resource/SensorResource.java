package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JAX-RS resource for managing sensors in the Smart Campus.
 * Provides endpoints for CRUD operations on sensors.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore dataStore = DataStore.getInstance();

    /**
     * GET method that returns all sensors.
     * Supports optional type query parameter to filter by sensor type.
     *
     * @param type optional sensor type filter
     * @return a list of sensors, filtered by type if provided
     */
    @GET
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> sensors = dataStore.getAllSensors();
        
        if (type != null && !type.isEmpty()) {
            return sensors.stream()
                    .filter(sensor -> sensor.getType().equals(type))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>(sensors);
    }

    /**
     * POST method that creates a new sensor.
     * Validates that the roomId exists in the DataStore.
     * Generates a UUID for the sensor id and adds it to the room's sensorIds list.
     *
     * @param sensor the sensor object to create (id will be overwritten with UUID)
     * @return Response with 201 Created status and Location header
     * @throws NotFoundException if the room specified by sensor.roomId doesn't exist
     */
    @POST
    public Response createSensor(Sensor sensor) {
        // Validate that the room exists
        Room room = dataStore.getRoom(sensor.getRoomId());
        
        if (room == null) {
            throw new NotFoundException("Room with id " + sensor.getRoomId() + " not found");
        }
        
        // Generate UUID for the new sensor
        String sensorId = UUID.randomUUID().toString();
        sensor.setId(sensorId);
        
        // Add sensor to DataStore
        dataStore.addSensor(sensor);
        
        // Add sensorId to the room's sensorIds list
        room.getSensorIds().add(sensorId);
        
        // Create Location header
        URI location = UriBuilder.fromResource(SensorResource.class)
                .path(SensorResource.class, "getSensor")
                .build(sensorId);
        
        return Response.created(location)
                .entity(sensor)
                .build();
    }

    /**
     * GET method that returns a specific sensor by id.
     *
     * @param sensorId the id of the sensor to retrieve
     * @return the sensor with the specified id
     * @throws NotFoundException if the sensor is not found (404)
     */
    @GET
    @Path("/{sensorId}")
    public Sensor getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensor(sensorId);
        
        if (sensor == null) {
            throw new NotFoundException("Sensor with id " + sensorId + " not found");
        }
        
        return sensor;
    }

    /**
     * DELETE method that removes a sensor.
     * Also removes the sensorId from the room's sensorIds list.
     *
     * @param sensorId the id of the sensor to delete
     * @return Response with 204 No Content if successful
     * @throws NotFoundException if the sensor is not found (404)
     */
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensor(sensorId);
        
        if (sensor == null) {
            throw new NotFoundException("Sensor with id " + sensorId + " not found");
        }
        
        // Remove sensorId from the room's sensorIds list
        String roomId = sensor.getRoomId();
        Room room = dataStore.getRoom(roomId);
        
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }
        
        // Remove the sensor
        dataStore.removeSensor(sensorId);
        
        return Response.noContent().build();
    }
}
