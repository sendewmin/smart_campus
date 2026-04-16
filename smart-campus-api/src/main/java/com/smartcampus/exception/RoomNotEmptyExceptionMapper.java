package com.smartcampus.exception;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        JsonObject errorResponse = Json.createObjectBuilder()
            .add("error", "conflict")
            .add("message", exception.getMessage())
            .build();
        
        return Response
            .status(Response.Status.CONFLICT)
            .entity(errorResponse)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
