package challenge.competer.domain.product.service;

import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.dto.ResponseDetailProductDto;
import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static challenge.competer.domain.product.productenum.MainCategory.*;
import static challenge.competer.domain.product.productenum.ProductState.IN_STOCK;
import static challenge.competer.domain.product.productenum.SubCategory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private Image defaultImage;
    private Image subImage1;
    private Image subImage2;

    private List<Product> productList = new ArrayList<>();

    @Mock
    ProductRepository productRepository;

    @Mock
    ImageRepository imageRepository;

    @InjectMocks
    ProductServiceImpl productServiceImpl;

    @BeforeEach
    void beforeEach() {
        for (int i = 4; i > 0; i--) {
            Product product = Product.builder()
                    .name("name" + i)
                    .price(i)
                    .content("content" + i)
                    .stockCount(i)
                    .productState(IN_STOCK)
                    .subCategory(LG)
                    .build();

            productList.add(product);
        }

        defaultImage = Image.builder()
                .id(1L)
                .productId(4L)
                .imageUrl("url")
                .build();

        subImage1 = Image.builder()
                .id(2L)
                .productId(4L)
                .imageUrl("subUrl1")
                .build();

        subImage2 = Image.builder()
                .id(3L)
                .productId(4L)
                .imageUrl("subUrl2")
                .build();

    }

    @Test
    @DisplayName("메인 페이지 상품 4개 내림차순 조회 테스트")
    void getMainPageProductsTest() {
        //given
        when(imageRepository.findFirstByProductId(any()))
                .thenReturn(Optional.of(defaultImage));
        when(productRepository.findTop4ByOrderByIdDesc())
                .thenReturn(productList);

        //when
        List<ResponseProductDto> findResponseProductDtos = productServiceImpl.getMainPageProducts();

        //then
        assertThat(findResponseProductDtos.size()).isEqualTo(4);
        assertThat(findResponseProductDtos.get(0).getPrice()).isEqualTo(4);
    }

    @Test
    @DisplayName("카테고리 페이지 상품 4개 내림차순 조회 테스트")
    void getCategoryPageProductsTest() {
        //given
        when(imageRepository.findFirstByProductId(any()))
                .thenReturn(Optional.of(defaultImage));
        when(productRepository.findTop4ByCategory(any(), any()))
                .thenReturn(productList);

        //when
        List<ResponseProductDto> findResponseProductDtos = productServiceImpl.getCategoryPageProducts(LAPTOP, LG);

        //then
        assertThat(findResponseProductDtos.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("상품 상세조회 테스트")
    void getDetailProductTest() {
        //given
        List<Image> images = new ArrayList<>();
        images.add(defaultImage);
        images.add(subImage1);
        images.add(subImage2);

        when(imageRepository.findAllByProductId(any()))
                .thenReturn(images);
        when(productRepository.findById(any()))
                .thenReturn(Optional.ofNullable(productList.get(0)));

        //when
        List<String> imageUrls = productServiceImpl.getDetailProduct(any()).getImageUrl();

        //then
        assertThat(imageUrls.size()).isEqualTo(3);
        assertThat(imageUrls.get(0)).isEqualTo("url");
    }

}