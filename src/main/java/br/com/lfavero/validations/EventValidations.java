package br.com.lfavero.validations;

import br.com.lfavero.entity.EventsEntity;
import br.com.lfavero.web.dto.request.CreateEventRequestDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;

@ApplicationScoped
public class EventValidations {

    public Boolean validateActiveEvent(CreateEventRequestDto event) {
        LocalDate today = LocalDate.now();
        return !event.dataInicialEvento.isAfter(today) && !event.dataFinalEvento.isBefore(today);
    }

    public void validateDatesCreateEvent(CreateEventRequestDto event) {
        if (event.dataInicialEvento.isAfter(event.dataFinalEvento)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("A data Inicial não pode ser maior que a data final.")
                            .build()
            );
        }
    }

    public void validateDatesUpdateEvent(EventsEntity entity) {
        if (entity.finalDate.isBefore(entity.initialDate)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("A data Inicial não pode ser maior que a data final.")
                            .build()
            );
        }
    }
}
