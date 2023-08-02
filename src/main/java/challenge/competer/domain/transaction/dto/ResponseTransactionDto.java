package challenge.competer.domain.transaction.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseTransactionDto {
    private Long point;
    private Integer price;
    private Long currentPoint;

    public ResponseTransactionDto(Long point, Integer price) {
        this.point = point;
        this.price = price;
        this.currentPoint = point - price;
    }
}
