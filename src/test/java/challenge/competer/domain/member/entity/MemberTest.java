package challenge.competer.domain.member.entity;

import challenge.competer.domain.member.role.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("유저가 결제하면 물건의 가격만큼 포인트가 차감된다.")
    @Test
    public void payPoint_O() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(1000L)
                .deposit(0L)
                .role(Role.MEMBER)
                .build();
        //when
        member.payPoint(1000);
        //then
        assertThat(member.getPoint()).isEqualTo(0);
    }

    @DisplayName("유저가 본인이 가진 포인트보다 비싼 상품을 결제하려 하면 예외가 발생한다.")
    @Test
    public void payPoint_X1() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(1000L)
                .deposit(0L)
                .role(Role.MEMBER)
                .build();
        //when
        //then
        Assertions.assertThatThrownBy(() -> member.payPoint(2000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저가 입찰한 물건이 낙찰되면 예치금만큼 포인트가 차감된다.")
    @Test
    public void subtractPointsOnBidSuccess_O1() throws Exception {
        Long point = 3000L;
        Long deposit = 1000L;
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(point)
                .deposit(deposit)
                .role(Role.MEMBER)
                .build();
        //when
        member.subtractPointsOnBidSuccess();

        //then
        assertThat(member.getPoint()).isEqualTo(point - deposit);
        assertThat(member.getDeposit()).isEqualTo(0);
    }

    @DisplayName("낙찰 후 잔액이 0보다 작으면 예외가 발생한다.")
    @Test
    public void subtractPointsOnBidSuccess_X1() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(1000L)
                .deposit(2000L)
                .role(Role.MEMBER)
                .build();
        //when
        //then
        assertThatThrownBy(() -> member.subtractPointsOnBidSuccess())
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("유저가 입찰한 물건이 낙찰되면 포인트가 차감된다.")
    @Test
    public void subtractPointsOnBidSuccess_O() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(1000L)
                .deposit(1000L)
                .role(Role.MEMBER)
                .build();
        //when
        member.subtractPointsOnBidSuccess();
        //then
        assertThat(member.getPoint()).isEqualTo(0);
    }

    @DisplayName("경매에 입찰하면 예치금이 증가한다.")
    @Test
    public void addPoint() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(2000L)
                .deposit(1000L)
                .role(Role.MEMBER)
                .build();
        //when
        member.setDeposit(2000L);
        //then
        assertThat(member.getDeposit()).isEqualTo(2000);
    }

    @DisplayName("보유한 포인트보다 높은 예치금을 입력하면 예외가 발생한다.")
    @Test
    public void addDeposit_X() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("username")
                .password("password")
                .point(2000L)
                .deposit(1000L)
                .role(Role.MEMBER)
                .build();
        //when
        //then
        Assertions.assertThatThrownBy(() -> member.setDeposit(2001L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}