package br.com.lfavero.controller;

import br.com.lfavero.service.EventsService;
import br.com.lfavero.web.dto.request.CreateEventRequestDto;
import br.com.lfavero.web.dto.request.UpdateEventRequestDto;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventsController {

    @Inject
    EventsService eventsService;

    @GET
    public Response findAllEvents(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize
    ) {
        return Response.ok(eventsService.findAll(page, pageSize)).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long eventId) {
        return Response.ok(eventsService.findById(eventId)).build();
    }

    @POST
    @Transactional
    public Response createEvent(@Valid CreateEventRequestDto event) {
        return Response.ok(eventsService.createEvent(event)).build();
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEvent(
            @PathParam("id") Long eventId,
            UpdateEventRequestDto infosEvent) {
        return Response.ok(eventsService.updateEvent(eventId, infosEvent)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response updateEvent(@PathParam("id") Long eventId) {
        eventsService.deleteEventById(eventId);
        return Response.noContent().build();
    }
}
