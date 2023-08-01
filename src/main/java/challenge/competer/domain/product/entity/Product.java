package challenge.competer.domain.product.entity;

import challenge.competer.domain.product.productenum.MainCategory;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.ProductType;
import challenge.competer.domain.product.productenum.SubCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer stockCount;

    @Column(nullable = false)
    private Integer likeCount;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ProductState productState;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MainCategory mainCategory;

    @Enumerated(STRING)
    @Column(nullable = false)
    private SubCategory subCategory;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Builder
    public Product(String name, Integer price, String content, Integer stockCount, ProductState productState, SubCategory subCategory, ProductType productType) {
        this.id = null;
        this.name = name;
        this.price = price;
        this.content = content;
        this.stockCount = stockCount;
        this.likeCount = 0;
        this.productState = productState;
        this.mainCategory = subCategory.getParentCategory();
        this.subCategory = subCategory;
        this.productType = productType;
    }
}
