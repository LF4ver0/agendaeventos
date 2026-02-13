package br.com.lfavero.validations;

import br.com.lfavero.web.dto.request.CreateEventRequestDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class EventValidations {

    public Boolean validateActiveEvent(CreateEventRequestDto event) {
        LocalDate today = LocalDate.now();
        return !event.dataInicialEvento.isAfter(today) && !event.dataFinalEvento.isBefore(today);
    }
}
