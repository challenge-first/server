package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.dto.RequestAuctionDto;
import challenge.competer.domain.auction.dto.ResponseAuctionDto;
import challenge.competer.domain.auction.dto.ResponseWinningPriceDto;
import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.image.entity.Image;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.member.role.Role;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private MemberRepository memberRepository;

    @InjectMocks
    private AuctionServiceImpl auctionServiceImpl;

    private Product product;
    private Image image;
    private Auction auction;
    private Member member1;
    private Member member2;
    private MemberDetails memberDetails1;
    private MemberDetails memberDetails2;

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
                .productId(product.getId())
                .openingPrice(3000L)
                .openingTime(LocalDateTime.now().withHour(15))
                .closingTime(LocalDateTime.now().withHour(16).withMinute(59))
                .winningPrice(6000L)
                .build();

        member1 = Member.builder()
                .id(1L)
                .point(20000L)
                .username("userA")
                .password("password")
                .role(Role.MEMBER)
                .deposit(0L)
                .build();

        member2 = Member.builder()
                .id(2L)
                .point(30000L)
                .username("userB")
                .password("password")
                .role(Role.MEMBER)
                .deposit(0L)
                .build();

        memberDetails1 = new MemberDetails(member1);
        memberDetails2 = new MemberDetails(member2);
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
    @DisplayName("입찰 성공 테스트1")
    public void bidSuccess1() {

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member1));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request = new RequestAuctionDto(12000L, LocalDateTime.now().withHour(15));

        ResponseWinningPriceDto response = auctionServiceImpl.bid(1L, request, memberDetails1);

        Assertions.assertThat(response.getWinningPrice()).isEqualTo(12000L);
    }

    @Test
    @DisplayName("입찰 성공 테스트2")
    public void bidSuccess2() {

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member2));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request = new RequestAuctionDto(20000L, LocalDateTime.now().withHour(15));

        ResponseWinningPriceDto response = auctionServiceImpl.bid(1L, request, memberDetails2);

        Assertions.assertThat(response.getWinningPrice()).isEqualTo(20000L);
    }

    @Test
    @DisplayName("현재 입찰가 실패시 발생하는 예외 테스트")
    public void bidFailCausedByWinningPrice() {

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member1));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request = new RequestAuctionDto(5000L, LocalDateTime.now().withHour(16));

        assertThatThrownBy(() -> auctionServiceImpl.bid(1L, request, memberDetails1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 입찰가보다 부족한 입찰 금액입니다");
    }

    @Test
    @DisplayName("기본 입찰가 실패시 발생하는 예외 테스트")
    public void bidFailCausedByOpeningPrice() {

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member1));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request = new RequestAuctionDto(2000L, LocalDateTime.now().withHour(16));

        assertThatThrownBy(() -> auctionServiceImpl.bid(1L, request, memberDetails1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기본 입찰가보다 부족한 입찰 금액입니다");
    }

    @Test
    @DisplayName("마감시간 초과시 발생하는 예외 테스트")
    public void bidFailCausedByClosingTime() {

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member1));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request = new RequestAuctionDto(10000L, LocalDateTime.now().withHour(17));

        assertThatThrownBy(() -> auctionServiceImpl.bid(1L, request, memberDetails1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("경매가 종료되었습니다");
    }

    @Test
    @DisplayName("보유 포인트 부족시 발생하는 예외 테스트")
    public void bidFailCausedByPoint() {

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member1));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request = new RequestAuctionDto(30000L, LocalDateTime.now().withHour(16));

        assertThatThrownBy(() -> auctionServiceImpl.bid(1L, request, memberDetails1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("포인트가 부족합니다");
    }

    @Test
    @DisplayName("입찰 경쟁 및 예치금 초기화 테스트")
    public void bidCompetitionAndInitDeposit() {

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member1));
        when(memberRepository.findById(2L))
                .thenReturn(Optional.of(member2));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request1 = new RequestAuctionDto(12000L, LocalDateTime.now().withHour(15));
        RequestAuctionDto request2 = new RequestAuctionDto(20000L, LocalDateTime.now().withHour(15));

        auctionServiceImpl.bid(1L, request1, memberDetails1);
        auctionServiceImpl.bid(1L, request2, memberDetails2);

        Assertions.assertThat(member1.getDeposit()).isEqualTo(0L);
        Assertions.assertThat(member2.getDeposit()).isEqualTo(20000L);
        Assertions.assertThat(auction.getMemberId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("경매 종료 후 회원 포인트 차감 테스트")
    public void winAuction() {

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member1));
        when(memberRepository.findById(2L))
                .thenReturn(Optional.of(member2));
        when(auctionRepository.findById(any()))
                .thenReturn(Optional.of(auction));
        when(auctionRepository.findByClosingTimeBetween(any(), any()))
                .thenReturn(Optional.of(auction));

        RequestAuctionDto request1 = new RequestAuctionDto(12000L, LocalDateTime.now().withHour(15));
        RequestAuctionDto request2 = new RequestAuctionDto(20000L, LocalDateTime.now().withHour(15));

        auctionServiceImpl.bid(1L, request1, memberDetails1);
        auctionServiceImpl.bid(1L, request2, memberDetails2);

        auctionServiceImpl.checkAndCloseAuctions();

        Assertions.assertThat(auction.getMemberId()).isEqualTo(2L);
        Assertions.assertThat(member1.getPoint()).isEqualTo(20000L);
        Assertions.assertThat(member2.getPoint()).isEqualTo(10000L);
    }
}