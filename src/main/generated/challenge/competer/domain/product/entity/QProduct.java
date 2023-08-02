package challenge.competer.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 2086000501L;

    public static final QProduct product = new QProduct("product");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final EnumPath<challenge.competer.domain.product.productenum.MainCategory> mainCategory = createEnum("mainCategory", challenge.competer.domain.product.productenum.MainCategory.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<challenge.competer.domain.product.productenum.ProductState> productState = createEnum("productState", challenge.competer.domain.product.productenum.ProductState.class);

    public final EnumPath<challenge.competer.domain.product.productenum.ProductType> productType = createEnum("productType", challenge.competer.domain.product.productenum.ProductType.class);

    public final NumberPath<Integer> stockCount = createNumber("stockCount", Integer.class);

    public final EnumPath<challenge.competer.domain.product.productenum.SubCategory> subCategory = createEnum("subCategory", challenge.competer.domain.product.productenum.SubCategory.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

