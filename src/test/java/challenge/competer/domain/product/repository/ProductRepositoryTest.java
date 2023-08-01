package challenge.competer.domain.product.repository;

import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.SubCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static challenge.competer.domain.product.productenum.ProductState.*;
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
                    .productState(IN_STOCK)
                    .subCategory(SubCategory.LG)
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
        assertThat(findProducts.get(0).getId()).isEqualTo(10);
    }

}