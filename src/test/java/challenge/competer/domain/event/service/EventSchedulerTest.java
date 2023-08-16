package challenge.competer.domain.event.service;

import challenge.competer.domain.event.entity.Event;
import challenge.competer.domain.event.repository.EventRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static challenge.competer.domain.event.eventstatus.EventStatus.CLOSE;
import static challenge.competer.domain.event.eventstatus.EventStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"event.schedule.cron = 0/2 * * * * *"})
class EventSchedulerTest {


    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventScheduler eventScheduler;

    @Test
    @DisplayName("오픈 되어 있는 Event 중, 이미 종료시간이 지난 이벤트가 있으면 status 변경")
    public void closingEventScheduledTest() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        Event event1 = Event.builder()
                .productId(1L)
                .productStock(1L)
                .discountRate(0.1)
                .eventStatus(OPEN)
                .currentMemberCount(1L)
                .maxMemberCount(2L)
                .openingTime(now.minusSeconds(10))
                .closingTime(now.minusSeconds(2))
                .build();

        Event event2 = Event.builder()
                .productId(1L)
                .productStock(1L)
                .discountRate(0.1)
                .eventStatus(OPEN)
                .currentMemberCount(1L)
                .maxMemberCount(2L)
                .openingTime(now)
                .closingTime(now.plusSeconds(2))
                .build();
        List<Event> events = List.of(event1, event2);

        eventRepository.saveAll(events);

        //when
        List<Event> eventScheduled = eventScheduler.closingEventScheduled();
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Event> eventsByEventStatus = eventRepository.findEventsByEventStatus();
                    assertThat(eventsByEventStatus).size().isEqualTo(0);
                });
    }
}