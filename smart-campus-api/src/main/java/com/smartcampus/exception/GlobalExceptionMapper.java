package com.smartcampus.exception;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        JsonObject errorResponse = Json.createObjectBuilder()
            .add("error", "internal_server_error")
            .add("message", "An unexpected error occurred")
            .build();
        
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(errorResponse)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
