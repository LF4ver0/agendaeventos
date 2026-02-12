package br.com.lfavero.scheduling;

import br.com.lfavero.entity.EventsEntity;
import br.com.lfavero.service.EventsService;
import br.com.lfavero.web.dto.response.EventResponseDto;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EventsScheduling {
//    @Inject
//    EventsService eventsService;

    private static final Logger LOG =
            Logger.getLogger(String.valueOf(EventsScheduling.class));

    @Scheduled(every = "25s")
    //@Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    void updateStatus() {
        LOG.info("Scheduler Iniciando: " + LocalDateTime.now());

        LocalDate today = LocalDate.now();

        List<EventsEntity> events = EventsEntity.findAll().list();

        for (EventsEntity event : events) {

            boolean activate =
                    !today.isBefore(event.initialDate) &&
                            !today.isAfter(event.finalDate);

            if (event.active != activate) {
                event.active = activate;
                event.persist();
            }
        }
        LOG.info("Scheduler Finalizado: " + LocalDateTime.now());
    }
}
