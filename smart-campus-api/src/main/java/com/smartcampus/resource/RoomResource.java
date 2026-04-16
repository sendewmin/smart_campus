package com.smartcampus.resource;

import com.smartcampus.model.Room;
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

/**
 * JAX-RS resource for managing rooms in the Smart Campus.
 * Provides endpoints for CRUD operations on rooms.
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore dataStore = DataStore.getInstance();

    /**
     * GET method that returns all rooms.
     *
     * @return a list of all rooms in the system
     */
    @GET
    public List<Room> getAllRooms() {
        Collection<Room> rooms = dataStore.getAllRooms();
        return new ArrayList<>(rooms);
    }

    /**
     * POST method that creates a new room.
     * Generates a UUID for the room id and adds it to the DataStore.
     *
     * @param room the room object to create (id will be overwritten with UUID)
     * @return Response with 201 Created status and Location header
     */
    @POST
    public Response createRoom(Room room) {
        // Generate UUID for the new room
        String roomId = UUID.randomUUID().toString();
        room.setId(roomId);
        
        // Add room to DataStore
        dataStore.addRoom(room);
        
        // Create Location header
        URI location = UriBuilder.fromResource(RoomResource.class)
                .path(RoomResource.class, "getRoom")
                .build(roomId);
        
        return Response.created(location)
                .entity(room)
                .build();
    }

    /**
     * GET method that returns a specific room by id.
     *
     * @param roomId the id of the room to retrieve
     * @return the room with the specified id
     * @throws NotFoundException if the room is not found (404)
     */
    @GET
    @Path("/{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        
        if (room == null) {
            throw new NotFoundException("Room with id " + roomId + " not found");
        }
        
        return room;
    }

    /**
     * DELETE method that removes a room.
     * Returns 409 Conflict if the room still has sensors.
     *
     * @param roomId the id of the room to delete
     * @return Response with 204 No Content if successful,
     *         or 409 Conflict if room has sensors
     * @throws NotFoundException if the room is not found (404)
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        
        if (room == null) {
            throw new NotFoundException("Room with id " + roomId + " not found");
        }
        
        // Check if room has sensors
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Cannot delete room with active sensors")
                    .build();
        }
        
        // Remove the room
        dataStore.removeRoom(roomId);
        
        return Response.noContent().build();
    }
}
