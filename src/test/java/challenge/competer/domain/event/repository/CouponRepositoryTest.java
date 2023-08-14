package challenge.competer.domain.event.repository;

import challenge.competer.domain.event.entity.Coupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void countByEventId() {
        //given

        Coupon coupon = Coupon.builder()
                .eventId(1L)
                .memberId(3L)
                .discountRate(10.0)
                .build();
        Coupon coupon2 = Coupon.builder()
                .eventId(1L)
                .memberId(2L)
                .discountRate(10.0)
                .build();
        Coupon coupon3 = Coupon.builder()
                .eventId(1L)
                .memberId(1L)
                .discountRate(10.0)
                .build();
        couponRepository.saveAll(List.of(coupon,coupon2,coupon3));
        //when
        Long count = couponRepository.countByEventId(1L).get();

        //then
        assertThat(count).isEqualTo(3);
    }
}