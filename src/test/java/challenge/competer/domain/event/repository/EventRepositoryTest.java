package challenge.competer.domain.event.repository;

import challenge.competer.domain.event.entity.Event;
import challenge.competer.domain.event.eventstatus.EventStatus;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Slf4j(topic = "eventRepositoryTest")
class EventRepositoryTest {

    LocalDateTime openingTime;
    LocalDateTime currentTime;
    LocalDateTime closingTime;

    @Autowired
    EventRepository eventRepository;

    @BeforeEach
    void beforeEach() {
        openingTime = LocalDateTime.now();
        closingTime = openingTime.plusSeconds(1000);

        Event event = Event.builder()
                .id(1L)
                .productId(2L)
                .maxMemberCount(100L)
                .currentMemberCount(0L)
                .productStock(10000L)
                .discountRate(10.0)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .eventStatus(EventStatus.OPEN)
                .discountRate(0.1)
                .build();

        eventRepository.save(event);
    }

    @Test
    @DisplayName("오픈되어있는 이벤트 조회 테스트")
    void findOpenEventTest() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 1);
        currentTime = openingTime.plusSeconds(100);

        //when
        List<Event> findEvent = eventRepository.findOpenEvent(currentTime, page);

        //then
        Assertions.assertThat(findEvent.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 조회 테스트 - 오픈된 이벤트 없음")
    void findEmptyOpenEventTest() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 1);
        currentTime = openingTime.plusSeconds(999999999);

        //when
        List<Event> findEvent = eventRepository.findOpenEvent(currentTime, page);

        //then
        Assertions.assertThat(findEvent.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("이미 종료되야 할 이벤트 status 변경")
    public void findOpenEventAfterClosingTime() throws Exception {
        //given
        openingTime = LocalDateTime.now().minusMinutes(10);
        closingTime = openingTime.plusSeconds(10);

        Event event = Event.builder()
                .id(2L)
                .productId(2L)
                .maxMemberCount(100L)
                .currentMemberCount(0L)
                .productStock(10000L)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .eventStatus(EventStatus.OPEN)
                .discountRate(0.1)
                .build();

        eventRepository.save(event);

        //when
        List<Event> eventsByEventStatus = eventRepository.findEventsByEventStatus();

        //then
        Event event1 = eventsByEventStatus.get(1);

        Assertions.assertThat(event1.getEventStatus()).isEqualTo(EventStatus.OPEN);
        Assertions.assertThat(event1.getClosingTime()).isBefore(LocalDateTime.now());
    }
}
