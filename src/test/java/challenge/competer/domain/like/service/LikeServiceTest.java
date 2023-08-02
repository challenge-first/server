package challenge.competer.domain.like.service;

import challenge.competer.domain.like.entity.Like;
import challenge.competer.domain.like.repository.LikeRepository;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.product.entity.Product;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.response.ResponseMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static challenge.competer.domain.member.role.Role.MEMBER;
import static challenge.competer.domain.product.productenum.ProductState.IN_STOCK;
import static challenge.competer.domain.product.productenum.ProductType.PRODUCT;
import static challenge.competer.domain.product.productenum.SubCategory.APPLE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    Member defaultMember;
    Product defaultProduct;
    Like defaultLike;

    @Mock
    LikeRepository likeRepository;

    @Mock
    MemberDetails memberDetails;

    @InjectMocks
    LikeServiceImpl likeService;

    @BeforeEach
    void beforeEach() {
        defaultMember = Member.builder()
                .username("username")
                .password("password")
                .point(0L)
                .role(MEMBER)
                .deposit(0L)
                .build();

        defaultProduct = Product.builder()
                .name("name")
                .price(10000)
                .content("content")
                .stockCount(10)
                .productState(IN_STOCK)
                .subCategory(APPLE)
                .productType(PRODUCT)
                .build();

        defaultLike = Like.builder()
                .memberId(defaultMember.getId())
                .productId(defaultProduct.getId())
                .build();
    }

    @Test
    @DisplayName("좋아요 등록 테스트")
    void toggleLikeTest() {
        //given
        when(likeRepository.save(any()))
                .thenReturn(defaultLike);
        when(likeRepository.findByMemberIdAndProductId(any(), any()))
                .thenReturn(Optional.empty());
        when(memberDetails.getId())
                .thenReturn(1L);

        //when
        ResponseMessageDto responseMessageDto = likeService.toggleLike(memberDetails, 2L);

        //then
        assertThat(responseMessageDto.getMessage()).isEqualTo("좋아요 완료");
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void toggleLikeCancelTest() {
        //given
        when(likeRepository.findByMemberIdAndProductId(any(), any()))
                .thenReturn(Optional.of(defaultLike));
        when(memberDetails.getId())
                .thenReturn(1L);

        //when
        ResponseMessageDto responseMessageDto = likeService.toggleLike(memberDetails, 2L);

        //then
        assertThat(responseMessageDto.getMessage()).isEqualTo("좋아요 취소");
    }

}