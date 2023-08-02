package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.ProductType;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ImageRepository;
import challenge.competer.domain.product.repository.ProductRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
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
                .openingPrice(3000L)
                .openingTime(LocalDateTime.now().minusMinutes(1))
                .closingTime(LocalDateTime.now().plusHours(1))
                .winningPrice(6000L)
                .build();
    }

    @Test
    @DisplayName("경매 정보 조회")
    void getAuctionTest() throws Exception {
        //given
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(imageRepository.findFirstByProductId(any()))
                .thenReturn(Optional.of(image));
        when(auctionRepository.findByCurrentTime(any()))
                .thenReturn(Optional.of(auction));

        //when
        ResponseAuctionDto responseDto = auctionServiceImpl.getAuction();
        //then
        assertThat(responseDto.getName()).isEqualTo("MacBook");

    }

    @Test
    @DisplayName("입찰 성공 테스트")
    public void bidSuccess() {

        RequestAuctionDto request = new RequestAuctionDto(10000L, LocalDateTime.now());

        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        ResponseWinningPriceDto response = auctionServiceImpl.bid(1L, request, memberDetails);

        Assertions.assertThat(response.getWinningPrice()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("현재 입찰가 실패 테스트")
    public void bidFailCausedByWinningPrice() {

        RequestAuctionDto request = new RequestAuctionDto(5000L, LocalDateTime.now());

        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        try {
            auctionServiceImpl.bid(1L, request, memberDetails);
        } catch (IllegalArgumentException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("현재 입찰가보다 부족한 입찰 금액입니다");
        }
    }

    @Test
    @DisplayName("기본 입찰가 실패 테스트")
    public void bidFailCausedByOpeningPrice() {

        RequestAuctionDto request = new RequestAuctionDto(2000L, LocalDateTime.now());

        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        try {
            auctionServiceImpl.bid(1L, request, memberDetails);
        } catch (IllegalArgumentException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("기본 입찰가보다 부족한 입찰 금액입니다");
        }
    }

    @Test
    @DisplayName("마감시간 초과 테스트")
    public void bidFailCausedByClosingTime() {

        RequestAuctionDto request = new RequestAuctionDto(10000L, LocalDateTime.now().plusHours(6));

        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        try {
            auctionServiceImpl.bid(1L, request, memberDetails);
        } catch (IllegalStateException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("경매가 종료되었습니다");
        }
    }
}