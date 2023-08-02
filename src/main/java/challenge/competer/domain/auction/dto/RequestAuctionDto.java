package challenge.competer.domain.auction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class RequestAuctionDto {

    private Long point;
    private LocalDateTime time;
}
