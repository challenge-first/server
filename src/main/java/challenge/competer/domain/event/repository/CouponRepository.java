package challenge.competer.domain.event.repository;

import challenge.competer.domain.event.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Long> countByEventId(Long eventId);
}
