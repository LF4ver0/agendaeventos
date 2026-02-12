package br.com.lfavero.service;

import br.com.lfavero.entity.EventsEntity;
import br.com.lfavero.entity.InstitutionEntity;
import br.com.lfavero.exception.EventNotFoundException;
import br.com.lfavero.mapper.EventMapper;
import br.com.lfavero.validations.EventValidations;
import br.com.lfavero.web.dto.request.CreateEventRequestDto;
import br.com.lfavero.web.dto.request.UpdateEventRequestDto;
import br.com.lfavero.web.dto.response.EventResponseDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@ApplicationScoped
public class EventsService {

    @Inject
    private EventMapper eventMapper;

    @Inject
    EventValidations eventValidations;

    public EventResponseDto createEvent(CreateEventRequestDto event) {
        if (event.institutionId == null) {
            throw new BadRequestException("Institution é obrigatória");
        }

        InstitutionEntity institution = InstitutionEntity.findById(event.institutionId);
        if (institution == null) {
            throw new NotFoundException("Institution não encontrada");
        }

        eventValidations.validateDatesCreateEvent(event);

        EventsEntity eventsEntity = new EventsEntity();
        eventsEntity.name = event.nomeEvento;
        eventsEntity.initialDate = event.dataInicialEvento;
        eventsEntity.finalDate = event.dataFinalEvento;
        eventsEntity.active = eventValidations.validateActiveEvent(event);
        eventsEntity.institution = institution;

        EventsEntity.persist(eventsEntity);

        return eventMapper.eventEntityToDto(eventsEntity);
    }

    public List<EventResponseDto> findAll(Integer page, Integer pageSize) {
        return eventMapper.eventListEntityToDto(EventsEntity.findAll().page(page, pageSize).list());
    }

    public EventResponseDto findById(Long eventId) {
        EventsEntity entity = (EventsEntity) EventsEntity
                .findByIdOptional(eventId)
                .orElseThrow(EventNotFoundException::new);

        return eventMapper.eventEntityToDto(entity);

    }

    public EventResponseDto updateEvent(Long eventId, UpdateEventRequestDto infosEvent) {
        if (infosEvent.nomeEvento == null && infosEvent.dataFinalEvento == null) {
            throw new BadRequestException("Informe ao menos um campo para atualização");
        }

        EventsEntity entity = (EventsEntity) EventsEntity
                .findByIdOptional(eventId)
                .orElseThrow(EventNotFoundException::new);

        if(infosEvent.nomeEvento != null){
            entity.name = infosEvent.nomeEvento;
        }

        if(infosEvent.dataFinalEvento != null) {
            entity.finalDate = infosEvent.dataFinalEvento;
        }
        eventValidations.validateDatesUpdateEvent(entity);

        return eventMapper.eventEntityToDto(entity);
    }

    public void deleteEventById(Long eventId) {
        EventResponseDto event = findById(eventId);

        EventsEntity.deleteById(event.idEvento);
    }

}
