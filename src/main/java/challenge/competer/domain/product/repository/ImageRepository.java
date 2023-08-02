package challenge.competer.domain.product.repository;

import challenge.competer.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByProductId(Long productId);

    Optional<Image> findFirstByProductId(Long productId);

}
