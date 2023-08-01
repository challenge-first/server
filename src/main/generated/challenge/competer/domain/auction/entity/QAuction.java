package challenge.competer.domain.auction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuction is a Querydsl query type for Auction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuction extends EntityPathBase<Auction> {

    private static final long serialVersionUID = -233929483L;

    public static final QAuction auction = new QAuction("auction");

    public final DateTimePath<java.time.LocalDateTime> closingTime = createDateTime("closingTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> openingPrice = createNumber("openingPrice", Long.class);

    public final DateTimePath<java.time.LocalDateTime> openingTime = createDateTime("openingTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Long> winningPrice = createNumber("winningPrice", Long.class);

    public QAuction(String variable) {
        super(Auction.class, forVariable(variable));
    }

    public QAuction(Path<? extends Auction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuction(PathMetadata metadata) {
        super(Auction.class, metadata);
    }

}

