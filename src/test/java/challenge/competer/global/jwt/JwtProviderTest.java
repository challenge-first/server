package challenge.competer.global.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import challenge.competer.domain.member.role.Role;
import challenge.competer.global.jwt.exception.TokenValidException;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(
            "ZDQ0NDYzMjJjMjc2Y2ExY2ZjMjYyYmY5MjcyYWNmNGIxZWYwMTBlM2Y2MzNkYjA1MjE5ODE2Mjc4MjU2NTBkZTBlNjViMjg5OWM1NzJjODMyZjVmOWRmNDYxYTZlMzAyOWVmNDQxMjgxMWJhMTEyN2IzNmE5ZjY5OTMwM2UyZTQNCg==",
            600000);
    }

    @DisplayName("Jwt Token이 생성된다.")
    @Test
    void creteJwt() throws Exception {
        //given
        String accessToken = jwtProvider.createAccessToken(1L, Role.MEMBER);

        assertThat(accessToken).isNotNull();
    }

    @DisplayName("토큰이 올바르게 파싱된다.")
    @Test
    void getPayload() throws Exception {
        //given
        Long memberId = 1L;
        Role role = Role.MEMBER;
        String createdToken = jwtProvider.createAccessToken(memberId, role);
        String accessToken = jwtProvider.extractToken(createdToken);
        assertThat(jwtProvider.getId(accessToken)).isEqualTo(memberId);
        assertThat(jwtProvider.getRole(accessToken)).isEqualTo(role.name());
    }

    @DisplayName("토큰이 없으면 예외가 발생한다.")
    @Test
    void getPayloadWithInvalidToken() throws Exception {

        assertThatThrownBy(() -> jwtProvider.extractToken(null))
            .hasMessage("유효하지 않은 JWT 입니다.")
            .isInstanceOf(TokenValidException.class);
    }

    @DisplayName("토큰의 인증타입이 Bearer 이 아니면 예외가 발생한다.")
    @Test
    void getPayloadWithInvalidToken2() throws Exception {
        Long memberId = 1L;
        Role role = Role.MEMBER;

        String accessToken = jwtProvider.createAccessToken(memberId, role);
        String token = jwtProvider.extractToken(accessToken);
        String invalidPrefix = "ABCDEF ";

        String invalidAccessToken = invalidPrefix+token;

        assertThatThrownBy(() -> jwtProvider.extractToken(invalidAccessToken))
            .hasMessage("유효하지 않은 JWT 입니다.")
            .isInstanceOf(TokenValidException.class);
    }

    @DisplayName("만료된 토큰은 예외가 발생한다.")
    @Test
    void expireToken() throws Exception {
        Long memberId = 1L;
        Role role = Role.MEMBER;
        JwtProvider invalidJwtProvider = new JwtProvider("ZDQ0NDYzMjJjMjc2Y2ExY2ZjMjYyYmY5MjcyYWNmNGIxZWYwMTBlM2Y2MzNkYjA1MjE5ODE2Mjc4MjU2NTBkZTBlNjViMjg5OWM1NzJjODMyZjVmOWRmNDYxYTZlMzAyOWVmNDQxMjgxMWJhMTEyN2IzNmE5ZjY5OTMwM2UyZTQNCg==", -100);

        String createdToken = invalidJwtProvider.createAccessToken(memberId, role);
        String invalidToken = jwtProvider.extractToken(createdToken);

        assertThatThrownBy(() -> jwtProvider.validateToken(invalidToken))
            .hasMessage("만료된 JWT 입니다.")
            .isInstanceOf(TokenValidException.class);
    }

    @DisplayName("잘못된 시크릿 키로 만들어진 토큰이 입력되면 예외가 발생한다.")
    @Test
    void invalidSecretKey() throws Exception {
        Long memberId = 1L;
        Role role = Role.MEMBER;
        JwtProvider invalidJwtProvider = new JwtProvider("ZDyZjVmOWRmNDYxYTZlMzAyOWVmNDQxMjgxMWJhMTEyN2IzNmE5ZjY5OTMwM2UyZTQNCg==", -100);

        String createdToken = invalidJwtProvider.createAccessToken(memberId, role);
        String invalidToken = jwtProvider.extractToken(createdToken);

        assertThatThrownBy(() -> jwtProvider.validateToken(invalidToken))
            .hasMessage("유효하지 않은 JWT 서명 입니다.")
            .isInstanceOf(TokenValidException.class);
    }
}
