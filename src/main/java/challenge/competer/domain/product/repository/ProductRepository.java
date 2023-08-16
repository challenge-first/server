package challenge.competer.domain.product.repository;

import challenge.competer.domain.product.entity.Product;
import challenge.competer.domain.product.productenum.MainCategory;
import challenge.competer.domain.product.productenum.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop4ByOrderByIdDesc();

    @Query("select p from Product p where p.mainCategory = :mainCategory and p.subCategory = :subCategory order by p.id desc limit 4")
    List<Product> findTop4ByCategory(@Param("mainCategory") MainCategory mainCategory, @Param("subCategory") SubCategory subCategory);

    @Query("select p from Product p where p.mainCategory = :mainCategory order by p.id desc limit 4")
    List<Product> findTop4ByMainCategory(@Param("mainCategory") MainCategory mainCategory);
}
