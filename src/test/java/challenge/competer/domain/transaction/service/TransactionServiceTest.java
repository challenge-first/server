package challenge.competer.domain.transaction.service;

import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.member.role.Role;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.productenum.SubCategory;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.domain.transaction.dto.RequestTransactionDto;
import challenge.competer.domain.transaction.dto.ResponseTransactionDto;
import challenge.competer.domain.transaction.entity.Transaction;
import challenge.competer.domain.transaction.repository.TransactionRepository;
import challenge.competer.domain.transaction.transactionstate.TransactionState;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static Member member;
    private static Member zeroPointMember;
    private static Product product;
    private static Product zeroStuckProduct;
    private static Transaction transaction;
    private static Transaction zeroPointFailTransaction;
    private static Transaction zeroStuckFailTransaction;
    private static RequestTransactionDto requestDto;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    MemberDetails memberDetails;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @BeforeAll
    public static void beforeAll() {
        int price = 10_000;

        product = Product.builder()
                .content("content")
                .productState(ProductState.IN_STOCK)
                .content("content")
                .productState(ProductState.IN_STOCK)
                .subCategory(SubCategory.APPLE)
                .price(10_000)
                .stockCount(2)
                .name("MacBook")
                .build();

        zeroStuckProduct = Product.builder()
                .content("content")
                .productState(ProductState.SOLD_OUT)
                .content("content")
                .productState(ProductState.SOLD_OUT)
                .subCategory(SubCategory.SAMSUNG)
                .price(10_000)
                .stockCount(0)
                .name("samsung")
                .build();

        member = Member.builder()
                .id(1L)
                .deposit(0L)
                .password("passwordA")
                .username("userA")
                .point(10_000L)
                .role(Role.MEMBER)
                .build();

        zeroPointMember = Member.builder()
                .id(2L)
                .deposit(0L)
                .password("passwordA")
                .username("userA")
                .point(0L)
                .role(Role.MEMBER)
                .build();

        transaction = Transaction.builder()
                .member(member)
                .product(product)
                .transactionTime(LocalDateTime.now())
                .transactionPrice(price)
                .transactionState(TransactionState.COMPLETED)
                .build();

        zeroPointFailTransaction = Transaction.builder()
                .member(zeroPointMember)
                .product(product)
                .transactionTime(LocalDateTime.now())
                .transactionPrice(price)
                .transactionState(TransactionState.FAILED)
                .build();

        zeroStuckFailTransaction = Transaction.builder()
                .member(member)
                .product(zeroStuckProduct)
                .transactionTime(LocalDateTime.now())
                .transactionPrice(price)
                .transactionState(TransactionState.FAILED)
                .build();

        requestDto = new RequestTransactionDto(price);
    }

    @Test
    @DisplayName("결제")
    void paymentTest() throws Exception {
        //given
        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(memberDetails.getId())
                .thenReturn(member.getId());
        when(transactionRepository.save(any()))
                .thenReturn(transaction);
        //when
        ResponseMessageDto responseMessageDto = transactionServiceImpl.payment(1L, requestDto, memberDetails);
        //then
        assertThat(responseMessageDto.getMessage()).isEqualTo("결제 완료");
    }

    @Test
    @DisplayName("결제 실패 - 포인트 잔액 부족")
    void paymentPointFailTest() throws Exception {
        //given
        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(zeroPointMember));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(memberDetails.getId())
                .thenReturn(zeroPointMember.getId());
        when(transactionRepository.save(any()))
                .thenReturn(zeroPointFailTransaction);
        //when
        ResponseMessageDto responseMessageDto = transactionServiceImpl.payment(1L, requestDto, memberDetails);
        //then
        assertThat(responseMessageDto.getMessage()).isEqualTo("포인트가 부족합니다.");
    }

    @Test
    @DisplayName("결제 실패 - 상품 재고 부족")
    void paymentStuckFailTest() throws Exception {
        //given
        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(zeroStuckProduct));
        when(memberDetails.getId())
                .thenReturn(member.getId());
        when(transactionRepository.save(any()))
                .thenReturn(zeroStuckFailTransaction);

        //when
        ResponseMessageDto responseMessageDto = transactionServiceImpl.payment(2L, requestDto, memberDetails);
        //then
        assertThat(responseMessageDto.getMessage()).isEqualTo("재고가 부족합니다.");
    }


    @Test
    @DisplayName("결제 전 유저 포인트, 가격, 결제후 잔액 표시")
    void getTransactionTest() throws Exception {
        //given
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(memberDetails.getPoint())
                .thenReturn(member.getPoint());

        //when
        ResponseDataDto<ResponseTransactionDto> responseDataDto = transactionServiceImpl.getTransaction(1L, memberDetails);
        //then
        assertThat(responseDataDto.getData().getCurrentPoint()).isEqualTo(0L);
    }
}