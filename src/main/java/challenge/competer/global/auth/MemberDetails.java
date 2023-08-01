package challenge.competer.global.auth;

import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.role.Role;
import lombok.Getter;

@Getter
public class MemberDetails {

    private Long id;

    private String username;

    private Long point;

    private Role role;

    private Long deposit;

    public MemberDetails(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.point = member.getPoint();
        this.role = member.getRole();
        this.deposit = member.getDeposit();
    }
}
