package challenge.competer.domain.product.service;

import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.dto.ResponseProductDto;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.MainCategory;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    Image findImage = imageRepository.findByProductId(product.getId())
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
                    Image findImage = imageRepository.findByProductId(product.getId())
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
}
