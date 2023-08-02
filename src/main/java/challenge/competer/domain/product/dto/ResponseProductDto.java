package challenge.competer.domain.product.dto;

import challenge.competer.domain.product.productenum.ProductState;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ResponseProductDto {

    private Long id;
    private String name;
    private Integer price;
    private ProductState productState;
    private Integer likeCount;
    private String imageUrl;

}
