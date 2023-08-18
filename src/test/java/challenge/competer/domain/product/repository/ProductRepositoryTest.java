package challenge.competer.domain.product.repository;

import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static challenge.competer.domain.product.productenum.MainCategory.*;
import static challenge.competer.domain.product.productenum.ProductState.*;
import static challenge.competer.domain.product.productenum.ProductType.*;
import static challenge.competer.domain.product.productenum.SubCategory.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 10; i++) {
            Product product = Product.builder()
                    .name("name" + i)
                    .price(i)
                    .content("content" + i)
                    .stockCount(i)
                    .image("i")
                    .productState(IN_STOCK)
                    .subCategory(LG)
                    .productType(PRODUCT)
                    .build();

            productRepository.save(product);
        }

    }

    @Test
    @DisplayName("메인 페이지 상품 4개 내림차순 조회 테스트")
    void findTop4ByOrderByIdDescTest() {
        //given, when
        List<Product> findProducts = productRepository.findTop4ByOrderByIdDesc();

        //then
        assertThat(findProducts.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("카테고리 페이지 상품 4개 내림차순 조회 테스트")
    void findTop4ByCategory() {
        //given, when
        List<Product> findProducts = productRepository.findTop4ByCategory(LAPTOP, LG);

        //then
        assertThat(findProducts.size()).isEqualTo(4);
        assertThat(findProducts.get(0).getMainCategory()).isEqualTo(LAPTOP);
        assertThat(findProducts.get(0).getSubCategory()).isEqualTo(LG);
    }

}