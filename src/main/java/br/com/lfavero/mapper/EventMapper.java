package br.com.lfavero.mapper;

import br.com.lfavero.entity.EventsEntity;
import br.com.lfavero.web.dto.response.EventResponseDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EventMapper {
    public EventResponseDto eventEntityToDto(EventsEntity event) {

        EventResponseDto dto = new EventResponseDto();

        dto.idEvento = event.id;
        dto.nomeEvento = event.name;
        dto.dataInicialEvento = event.initialDate;
        dto.dataFinalEvento = event.finalDate;
        dto.eventoAtivo=event.active;

        if (event.institution != null) {
            dto.idInstituicao = event.institution.id;
            dto.institutionName = event.institution.name;
        }
        return dto;
    }

    public List<EventResponseDto> eventListEntityToDto(List<EventsEntity> events) {
        List<EventResponseDto> list = new ArrayList<>();
        for (EventsEntity event : events) {
            EventResponseDto dto = eventEntityToDto(event);
            list.add(dto);
        }
        return list;
    }
}
