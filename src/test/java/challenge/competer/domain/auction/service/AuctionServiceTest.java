package challenge.competer.domain.auction.service;

import challenge.competer.domain.auction.entity.Auction;
import challenge.competer.domain.auction.repository.AuctionRepository;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.member.role.Role;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.global.auth.MemberDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class AuctionServiceTest {

    private static Product product;

    private static Member member;

    private static Auction auction;

    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;
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

        member = Member.builder()
                .id(1L)
                .username("userA")
                .password("passwordA")
                .deposit(0L)
                .point(10000L)
                .role(Role.MEMBER)
                .build();

        auction = Auction.builder()
                .id(1L)
                .memberId(1L)
                .openingPrice(5000L)
                .openingTime(LocalDateTime.of(2020,12,17,6,30))
                .closingTime(LocalDateTime.of(2024,12,17,6,30))
                .winningPrice(9000L)
                .build();
    }


    @Test
    @DisplayName("Normal bid")
    public void normal() {
        Mockito.when()
    }



}