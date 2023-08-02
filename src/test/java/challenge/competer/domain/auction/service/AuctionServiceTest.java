package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.ProductType;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.domain.transaction.service.TransactionServiceImpl;
import challenge.competer.global.auth.MemberDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    MemberDetails memberDetails;

    @InjectMocks
    private AuctionServiceImpl auctionServiceImpl;

    private Product product;
    private Image image;
    private Auction auction;

    @BeforeEach
    public void beforeEach() {
        product = Product.builder()
                .productType(ProductType.AUCTION_PRODUCT)
                .stockCount(1)
                .subCategory(SubCategory.APPLE)
                .productState(ProductState.IN_STOCK)
                .price(100)
                .name("MacBook")
                .content("content")
                .build();

        image = Image.builder()
                .id(1L)
                .imageUrl("url")
                .productId(product.getId())
                .build();

        auction = Auction.builder()
                .id(1L)
                .memberId(1L)
                .productId(product.getId())
                .openingPrice(100L)
                .openingTime(LocalDateTime.now().minusMinutes(1))
                .closingTime(LocalDateTime.now().plusHours(1))
                .winningPrice(100L)
                .build();
    }
}