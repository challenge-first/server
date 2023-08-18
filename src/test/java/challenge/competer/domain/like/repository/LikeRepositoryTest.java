package challenge.competer.domain.like.repository;

import challenge.competer.domain.like.entity.Like;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static challenge.competer.domain.member.role.Role.*;
import static challenge.competer.domain.product.productenum.ProductState.*;
import static challenge.competer.domain.product.productenum.ProductType.*;
import static challenge.competer.domain.product.productenum.SubCategory.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeRepositoryTest {

    Member defaultMember;
    Product defaultProduct;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .username("username")
                .password("password")
                .point(0L)
                .role(MEMBER)
                .deposit(0L)
                .build();

        defaultMember = memberRepository.save(member);

        Product product = Product.builder()
                .name("name")
                .price(10000)
                .content("content")
                .stockCount(10)
                .image("image")
                .productState(IN_STOCK)
                .subCategory(APPLE)
                .productType(PRODUCT)
                .build();

        defaultProduct = productRepository.save(product);

        Like like = Like.builder()
                .memberId(defaultMember.getId())
                .productId(defaultProduct.getId())
                .build();

        likeRepository.save(like);
    }

    @Test
    @DisplayName("좋아요 조회 테스트")
    void findByMemberIdAndProductIdTest() {
        //given
        Long memberId = defaultMember.getId();
        Long productId = defaultProduct.getId();

        //when
        Optional<Like> findLike = likeRepository.findByMemberIdAndProductId(memberId, productId);

        //then
        assertThat(findLike).isNotEmpty();
    }

}