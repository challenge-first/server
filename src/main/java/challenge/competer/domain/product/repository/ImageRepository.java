package challenge.competer.domain.product.repository;

import challenge.competer.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByProductId(Long productId);

}
