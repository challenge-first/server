package challenge.competer.domain.auction.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseAuctionDto {
    private String name;
    private String imageUrl;
    private Long openingPrice;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private Long winningPrice;
}
