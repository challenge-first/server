package challenge.competer.global.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import challenge.competer.domain.member.role.Role;
import challenge.competer.global.jwt.exception.TokenValidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {
    private static final String TOKEN_TYPE = "Bearer ";
    private final Key secretKey;
    private final long TOKEN_EXPIRES_IN;
    private final String HEADER_NAME = "Authorization";
    private final String AUTHORIZATION_KEY = "auth";

    public JwtProvider(
        @Value("${jwt.secret.key}") final String secretKey,
        @Value("${jwt.expire.length}") final long tokenExpiresIn
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.TOKEN_EXPIRES_IN = tokenExpiresIn;
    }

    public String createAccessToken(final Long id, final Role role) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + TOKEN_EXPIRES_IN);
        return TOKEN_TYPE + Jwts.builder()
            .setSubject(id.toString())
            .setIssuedAt(now)
            .setExpiration(validity)
            .claim(AUTHORIZATION_KEY, role)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public void validateToken(final String token) {
        try {
            getClaimsJws(token);
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("유효하지 않은 JWT 서명 입니다.");
            throw new TokenValidException("유효하지 않은 JWT 서명 입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 입니다.");
            throw new TokenValidException("만료된 JWT 입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 입니다.");
            throw new TokenValidException("지원되지 않는 JWT 입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 JWT 입니다.");
            throw new TokenValidException("유효하지 않은 JWT 입니다.", e);
        } catch (RuntimeException e){
            log.error("알 수 없는 오류가 발생했습니다.");
            throw new TokenValidException("알 수 없는 오류가 발생했습니다.", e);
        }
    }

    public String getRole(final String token) {
        return getClaimsJws(token)
            .getBody()
            .get(AUTHORIZATION_KEY, String.class);
    }

    public Long getId(final String token) {
        String idValue = getClaimsJws(token)
            .getBody()
            .getSubject();
        return Long.parseLong(idValue);
    }

    private Jws<Claims> getClaimsJws(final String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);
    }

    public String extractToken(final String token) {
        if (isInvalidTokenType(token)) {
            throw new TokenValidException("유효하지 않은 JWT 입니다.");
        }
        return token.substring(TOKEN_TYPE.length());
    }

    private boolean isInvalidTokenType(final String token) {
        return token == null || !token.startsWith(TOKEN_TYPE);
    }

    public String getToken(final HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }

    public String getHEADER_NAME() {
        return HEADER_NAME;
    }
}
