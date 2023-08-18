package challenge.competer.domain.event.service;

import challenge.competer.domain.event.entity.Event;
import challenge.competer.domain.event.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class EventScheduler {

    private final EventRepository eventRepository;

    public EventScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.schedule.cron}")
    @Transactional
    public List<Event> closingEventScheduled() {
        List<Event> openEvent = eventRepository.findEventsByEventStatus();
        LocalDateTime now = LocalDateTime.now();
        for (Event event : openEvent) {
            if (event.getClosingTime().isBefore(now)) {
                event.closeEventAtClosingTime();
            }
        }
        System.out.println("openEvent.size() =" + openEvent.size());
        return openEvent;
    }
}
