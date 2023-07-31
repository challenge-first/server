package challenge.competer.domain.event.entity;

import challenge.competer.domain.event.eventstatus.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "events")
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long maxMemberCount;

    @Column(nullable = false)
    private Long currentMemberCount;

    @Column(nullable = false)
    private Long productStock;

    @Column(nullable = false)
    private LocalDateTime openingTime;

    @Column(nullable = false)
    private LocalDateTime closingTime;

    @Column(nullable = false)
    @Enumerated(STRING)
    private EventStatus eventStatus;



}
