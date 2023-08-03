package challenge.competer.domain.member.service;

import challenge.competer.domain.member.dto.RequestMemberLoginDto;
import challenge.competer.domain.member.dto.RequestMemberPointDto;
import challenge.competer.domain.member.dto.ResponsePointDto;
import challenge.competer.domain.member.dto.ResponseTokenDto;
import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.exception.PasswordMismatchException;
import challenge.competer.domain.member.exception.UserNotFoundException;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.domain.member.role.Role;
import challenge.competer.global.auth.MemberDetails;
import challenge.competer.global.jwt.JwtProvider;
import challenge.competer.global.response.ResponseMessageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;


    @DisplayName("로그인에 성공한다.")
    @Test
    public void login_Success_Test() {
        //given
        String memberId = "username";
        String password = "password";
        Long id = 1L;
        Member member = createMember(id, memberId, password, 0L);

        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(member));
        when(jwtProvider.createAccessToken(anyLong(), any()))
                .thenReturn("AccessToken");

        RequestMemberLoginDto request = new RequestMemberLoginDto(memberId, password);

        //when
        ResponseTokenDto response = memberService.login(request);

        //then
        assertThat(response.getHeaderName()).isEqualTo(jwtProvider.getHEADER_NAME());
        assertThat(response.getAccessTokenValue()).isEqualTo("AccessToken");
    }

    @DisplayName("존재하지 않는 사용자 정보입력으로 인해 로그인에 실패한다.")
    @Test
    public void login_Fail_Test_UserNotFound() {
        //given
        String memberId = "invalidUsername";
        String password = "password";

        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        RequestMemberLoginDto request = new RequestMemberLoginDto(memberId, password);

        // when then
        assertThatThrownBy(() -> memberService.login(request))
                .hasMessage("회원을 찾을 수 없습니다.")
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("잘못된 비밀번호 입력으로 인해 로그인에 실패한다.")
    @Test
    public void login_Fail_Test_PasswordMismatch() {
        //given
        Long id = 1L;
        String memberId = "username";
        String invalidPassword = "invalidPassword";
        String validPassword = "password";
        Member member = createMember(id, memberId, validPassword, 0L);

        when(memberRepository.findByUsername(memberId))
                .thenReturn(Optional.of(member));

        RequestMemberLoginDto request = new RequestMemberLoginDto(memberId, invalidPassword);

        // when then
        assertThatThrownBy(() -> memberService.login(request))
                .hasMessage("비밀번호가 일치하지 않습니다.")
                .isInstanceOf(PasswordMismatchException.class);
    }

    @DisplayName("포인트 충전에 성공한다.")
    @Test
    public void addPoint_success_test() {
        //given
        Long id = 1L;
        String memberId = "memberId";
        String password = "password";
        Long currentPoint = 0L;
        Long addPoint = 5000L;
        Member member = createMember(id, memberId, password, currentPoint);

        MemberDetails memberDetails = new MemberDetails(member);

        when(memberRepository.findById(memberDetails.getId()))
                .thenReturn(Optional.ofNullable(member));

        RequestMemberPointDto request = new RequestMemberPointDto(addPoint);

        //when
        ResponseMessageDto response = memberService.addPoint(request, memberDetails);

        //then
        assertThat(response)
                .extracting("message", "statusCode", "statusMessage")
                .contains("충전 완료", HttpStatus.OK.value(), HttpStatus.OK.toString());

        assertThat(member.getPoint()).isEqualTo(currentPoint + addPoint);

        verify(memberRepository, times(1)).findById(memberDetails.getId());
    }

    @DisplayName("포인트 조회에 성공한다.")
    @Test
    public void getPoint_success_test(){
        //given
        Long memberId = 1L;
        String memberName = "memberName";
        String password = "password";
        Long currentPoint = 5000L;
        Member member = createMember(memberId, memberName, password, currentPoint);
        MemberDetails memberDetails = new MemberDetails(member);

        //when
        ResponsePointDto response = memberService.getPoint(memberDetails);

        //then
        assertThat(response.getPoint()).isEqualTo(currentPoint);
    }

    private static Member createMember(Long memberId, String memberName, String password, Long currentPoint) {
        Member member = Member.builder()
                .id(memberId)
                .username(memberName)
                .password(password)
                .point(currentPoint)
                .deposit(0L)
                .role(Role.MEMBER)
                .build();
        return member;
    }
}