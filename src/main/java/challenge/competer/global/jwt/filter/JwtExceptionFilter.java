package challenge.competer.global.jwt.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import challenge.competer.domain.member.exception.UserNotFoundException;
import challenge.competer.global.jwt.exception.TokenValidException;
import challenge.competer.global.response.ResponseMessageDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Order(1)
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (TokenValidException | UserNotFoundException e){
            handleException(response, e.getMessage());
        }
    }

    private void handleException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ResponseMessageDto responseBody = new ResponseMessageDto(message,HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString());
        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
