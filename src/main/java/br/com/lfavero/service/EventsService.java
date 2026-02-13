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
import jakarta.validation.ValidationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class EventsService {

    private static final Logger LOG = Logger.getLogger(EventsService.class);

    @Inject
    private EventMapper eventMapper;

    @Inject
    EventValidations eventValidations;

    public EventResponseDto createEvent(CreateEventRequestDto event) {
        if (event.institutionId == null) {
            LOG.warn("Institution não informada");
            throw new BadRequestException("Institution é obrigatória");
        }

        InstitutionEntity institution = InstitutionEntity.findById(event.institutionId);
        if (institution == null) {
            LOG.warnf(
                    "Institution não encontrada: id=%d",
                    event.institutionId
            );
            throw new NotFoundException("Institution não encontrada");
        }

        LOG.infof(
                "Criando evento: nome=%s, institutionId=%d",
                event.nomeEvento,
                event.institutionId
        );

        EventsEntity eventsEntity = new EventsEntity();
        eventsEntity.name = event.nomeEvento;
        eventsEntity.initialDate = event.dataInicialEvento;
        eventsEntity.finalDate = event.dataFinalEvento;
        eventsEntity.active = eventValidations.validateActiveEvent(event);
        eventsEntity.institution = institution;

        EventsEntity.persist(eventsEntity);

        LOG.infof(
                "Evento criado: id=%d",
                eventsEntity.id
        );

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

        LOG.infof("Atualizando evento ID= %d", eventId);

        if(infosEvent.nomeEvento != null){
            entity.name = infosEvent.nomeEvento;
        }

        if(infosEvent.dataFinalEvento != null) {
            if (infosEvent.dataFinalEvento.isBefore(entity.initialDate)) {
                throw new ValidationException(
                        String.format(
                        "A data final (%s) não pode ser anterior à data inicial (%s)",
                        infosEvent.dataFinalEvento,
                        entity.initialDate)
                );
            }
            entity.finalDate = infosEvent.dataFinalEvento;
        }

        return eventMapper.eventEntityToDto(entity);
    }

    public void deleteEventById(Long eventId) {
        LOG.infof("Removendo evento id=%d", eventId);

        EventResponseDto event = findById(eventId);

        EventsEntity.deleteById(event.idEvento);
    }
}
