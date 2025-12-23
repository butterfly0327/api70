package com.yumyumcoach.global.jwt;

import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Collections;

/*
모든 요청이 들어올 때마다 JWT 를 검사하고 사용자 인증을 해줌
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            // 인증이 필요 없는 경로면 그냥 통과(로그인, 회원가입 등)
            String uri = request.getRequestURI();
            if(uri.startsWith("/api/auth/sign-in") || uri.startsWith("/api/auth/sign-up") ||
                uri.startsWith("/api/auth/check-email") || uri.startsWith("/api/auth/check-username") ||
                uri.startsWith("/api/auth/refresh") || uri.startsWith("/api/exercises/search")) {
                filterChain.doFilter(request,response);
                return;
            }

            // ✅ 이미 인증이 세팅되어 있으면(JWT든 dev든) 그대로 통과
            Authentication existing = SecurityContextHolder.getContext().getAuthentication();
            if (existing != null && existing.isAuthenticated()
                    && !"anonymousUser".equals(String.valueOf(existing.getPrincipal()))) {
                filterChain.doFilter(request, response);
                return;
            }

            // Authorization 헤더에서 Bearer 토큰 추출
            String token = null;
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }

            // 토큰이 없으면 인증 불가(로그인하지 않은 사용자)
            if (token == null) {
                resolver.resolveException(request, response, null, new BusinessException(ErrorCode.AUTH_UNAUTHORIZED));
                return;
            }
            
            // 토큰 검증
            jwtTokenProvider.validateToken(token);

            // 토큰에서 이메일 추출
            String email = jwtTokenProvider.getEmail(token);

            // SecurityContext 에 인증 정보 세팅
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 다음 필터/컨트롤러로 진행
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            resolver.resolveException(request, response, null,
                    new BusinessException(ErrorCode.AUTH_UNAUTHORIZED, "액세스 토큰이 만료되었습니다."));
        } catch (Exception e) {
            resolver.resolveException(request, response, null,
                    new BusinessException(ErrorCode.AUTH_UNAUTHORIZED, "유효하지 않은 토큰입니다."));
        }
    }
}
