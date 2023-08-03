package challenge.competer.domain.event.repository;

import challenge.competer.domain.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.openingTime < :currentTime and e.closingTime > :currentTime order by e.id desc")
    List<Event> findOpenEvent(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
}
