package challenge.competer.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "images")
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String imageUrl;

}
