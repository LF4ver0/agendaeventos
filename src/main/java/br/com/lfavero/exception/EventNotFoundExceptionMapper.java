package br.com.lfavero.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EventNotFoundExceptionMapper implements ExceptionMapper <EventNotFoundException> {
    @Override
    public Response toResponse(EventNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Evento Não Encontrado").build();
    }
}
