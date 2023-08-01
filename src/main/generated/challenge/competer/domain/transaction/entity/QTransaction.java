package challenge.competer.domain.transaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = -1724190955L;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Integer> transactionPrice = createNumber("transactionPrice", Integer.class);

    public final EnumPath<challenge.competer.domain.transaction.transactionstate.TransactionState> transactionState = createEnum("transactionState", challenge.competer.domain.transaction.transactionstate.TransactionState.class);

    public final DateTimePath<java.time.LocalDateTime> transactionTime = createDateTime("transactionTime", java.time.LocalDateTime.class);

    public QTransaction(String variable) {
        super(Transaction.class, forVariable(variable));
    }

    public QTransaction(Path<? extends Transaction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransaction(PathMetadata metadata) {
        super(Transaction.class, metadata);
    }

}

