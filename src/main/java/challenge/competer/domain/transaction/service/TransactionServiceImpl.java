package challenge.competer.domain.transaction.service;

import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.ProductState;
import challenge.competer.domain.product.repository.ProductRepository;
import challenge.competer.domain.transaction.dto.RequestTransactionDto;
import challenge.competer.domain.transaction.dto.ResponseTransactionDto;
import challenge.competer.domain.transaction.entity.Transaction;
import challenge.competer.domain.transaction.repository.TransactionRepository;
import challenge.competer.domain.transaction.transactionstate.TransactionState;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseDataDto;
import challenge.competer.global.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static challenge.competer.domain.transaction.transactionstate.TransactionState.*;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "TransactionServiceImpl")
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseMessageDto payment(Long productId, RequestTransactionDto requestDto, MemberDetails memberDetails) {
        Member member = findMemberById(memberDetails.getId());
        Product product = findProductById(productId);

        try {
            pointNotEnough(member.getPoint(), requestDto.getPrice());
            hasStock(product);

            Transaction transaction = saveTransaction(member, product, COMPLETED, requestDto.getPrice());
            transaction.getMember().payPoint(requestDto.getPrice());
            transaction.getProduct().decreaseStockCount();

            return new ResponseMessageDto("결제 완료", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (IllegalArgumentException e) {
            saveTransaction(member, product, FAILED, requestDto.getPrice());
            return new ResponseMessageDto(e.getMessage(), HttpStatus.PAYMENT_REQUIRED.value(), HttpStatus.PAYMENT_REQUIRED.getReasonPhrase());
        }
    }

        @Override
        @Transactional(readOnly = true)
        public ResponseDataDto<ResponseTransactionDto> getTransaction(Long productId, MemberDetails memberDetails) {
            Product product = findProductById(productId);
            ResponseTransactionDto transactionDto = new ResponseTransactionDto(memberDetails.getPoint(), product.getPrice());
        return new ResponseDataDto<>(transactionDto);
    }

    private Transaction saveTransaction(Member member, Product product, TransactionState state, int price) {
        Transaction transaction = Transaction.builder()
                .member(member)
                .product(product)
                .transactionPrice(price)
                .transactionTime(LocalDateTime.now())
                .transactionState(state)
                .build();

        return transactionRepository.save(transaction);
    }

    private void pointNotEnough(Long point, int price) {
        if (point < price) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

    private void hasStock(Product product) {
        if (product.getProductState().equals(ProductState.SOLD_OUT)) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
    }

    private Member findMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다."));
        return member;
    }

    private Product findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다."));
        return product;
    }
}
