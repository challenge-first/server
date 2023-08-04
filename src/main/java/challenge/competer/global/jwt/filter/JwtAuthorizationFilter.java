package challenge.competer.global.jwt.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import challenge.competer.domain.member.entity.Member;
import challenge.competer.domain.member.exception.UserNotFoundException;
import challenge.competer.domain.member.repository.MemberRepository;
import challenge.competer.global.jwt.JwtProvider;
import challenge.competer.global.auth.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Order(2)
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (CorsUtils.isPreFlightRequest(request)){
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Headers","*");
            return;
        }

        String tokenValue = jwtProvider.getToken(request);
        String token = jwtProvider.extractToken(tokenValue);

        jwtProvider.validateToken(token);

        Long memberId = jwtProvider.getId(token);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        MemberDetails memberDetails = new MemberDetails(member);
        request.setAttribute("member", memberDetails);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getRequestURI();
        return servletPath.startsWith("/members/login") || servletPath.startsWith("/h2-console") || servletPath.startsWith("/products");
    }
}
