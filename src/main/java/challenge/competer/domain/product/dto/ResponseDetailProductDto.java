package challenge.competer.domain.product.dto;

import challenge.competer.domain.product.productenum.ProductState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ResponseDetailProductDto {

    private String name;
    private Integer price;
    private String content;
    private Integer stockCount;
    private ProductState productState;
    private String imageUrl;
    private Integer likeCount;

}
