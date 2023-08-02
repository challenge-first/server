package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.SubCategory;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    private  Product product;

    private  Auction auction;

    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    MemberDetails memberDetails;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @BeforeEach
    public void init() {

        product = Product.builder()
                .content("content")
                .productState(ProductState.IN_STOCK)
                .subCategory(SubCategory.APPLE)
                .price(10000)
                .name("MacBook")
                .build();


        auction = Auction.builder()
                .id(1L)
                .productId(product.getId())
                .memberId(1L)
                .openingPrice(5000L)
                .openingTime(LocalDateTime.now().minusHours(1))
                .closingTime(LocalDateTime.now().plusHours(1))
                .winningPrice(9000L)
                .build();
    }

    @Test
    @DisplayName("bid Success")
    public void bidSuccess() {

        RequestAuctionDto request = new RequestAuctionDto(10000L, LocalDateTime.now());

        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        ResponseWinningPriceDto response = auctionService.bid(1L, request, memberDetails);

        Assertions.assertThat(response.getWinningPrice()).isEqualTo(10000L);
    }
}