package challenge.competer.domain.event.dto;

import challenge.competer.domain.event.eventstatus.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ResponseEventDto {

    private String name;
    private Integer price;
    private String imageUrl;
    private Long maxMemberCount;
    private Long currentMemberCount;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private Long productStock;
    private EventStatus eventStatus;

}
