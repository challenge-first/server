package challenge.competer.domain.member.entity;

import challenge.competer.domain.member.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "members")
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    private Long deposit;

    public void rechargePoint(Long point) {
        this.point += point;
    }

    public void payPoint(int price) {
        Long availablePoint = getAvailablePoint();
        Long balance = availablePoint - price;
        if (balance < 0) {
            throw new IllegalArgumentException();
        }
        this.point = balance;
    }

    public void subtractPointsOnBidSuccess() {
        Long balance = this.point - this.deposit;
        if (balance < 0) {
            throw new IllegalArgumentException();
        }
        this.point = balance;
        resetDeposit();
    }

    public void setDeposit(Long bid) {
        if (point < bid) {
            throw new IllegalArgumentException();
        }
        this.deposit = bid;
    }

    public Long getAvailablePoint() {
        if (point - deposit < 0) {
            throw new IllegalArgumentException();
        }
        return point - deposit;
    }

    public void resetDeposit() {
        this.deposit = 0L;
    }
}
