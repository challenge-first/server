package challenge.competer.domain.product.service;

import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.dto.ResponseDetailProductDto;
import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.MainCategory;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Override
    public List<ResponseProductDto> getMainPageProducts() {
        List<Product> findProducts = productRepository.findTop4ByOrderByIdDesc();

        return findProducts.stream()
                .map(product -> {
                    Image findImage = imageRepository.findFirstByProductId(product.getId())
                            .orElseThrow(() -> new IllegalArgumentException("이미지가 없습니다."));

                    return createResponseProductDto(product, findImage);
                })
                .toList();
    }

    @Override
    public List<ResponseProductDto> getCategoryPageProducts(MainCategory mainCategory, SubCategory subCategory) {
        List<Product> findProducts = productRepository.findTop4ByCategory(mainCategory, subCategory);

        return findProducts.stream()
                .map(product -> {
                    Image findImage = imageRepository.findFirstByProductId(product.getId())
                            .orElseThrow(() -> new IllegalArgumentException("이미지가 없습니다."));

                    return createResponseProductDto(product, findImage);
                })
                .toList();
    }

    private ResponseProductDto createResponseProductDto(Product product, Image findImage) {
        return ResponseProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .productState(product.getProductState())
                .likeCount(product.getLikeCount())
                .imageUrl(findImage.getImageUrl())
                .build();
    }

    @Override
    public ResponseDetailProductDto getDetailProduct(Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
        List<Image> findImages = imageRepository.findAllByProductId(productId);
        List<String> images = new ArrayList<>();

        if (findImages.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }

        findImages.forEach(image -> images.add(image.getImageUrl()));

        return createResponseDetailProductDto(findProduct, images);
    }

    private static ResponseDetailProductDto createResponseDetailProductDto(Product findProduct, List<String> images) {
        return ResponseDetailProductDto.builder()
                .name(findProduct.getName())
                .price(findProduct.getPrice())
                .content(findProduct.getContent())
                .stockCount(findProduct.getStockCount())
                .productState(findProduct.getProductState())
                .imageUrl(images)
                .likeCount(findProduct.getLikeCount())
                .build();
    }
}
