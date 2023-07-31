package challenge.competer.domain.transaction.entity;

import challenge.competer.domain.transaction.transactionstate.TransactionState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "transactions")
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer transactionPrice;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TransactionState transactionState;

}
