package challenge.competer.domain.like.repository;

import challenge.competer.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndProductId(Long memberId, Long productId);

}
