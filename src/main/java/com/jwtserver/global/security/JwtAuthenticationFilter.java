package com.jwtserver.global.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

/*
	API 를 호출할 때 사용되는 인증필터 입니다.
*/
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtAuthenticationProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletRequest) servletRequest).getHeader("Authorization");

        // 토큰 유효성 검증 후 유효한 토큰이라면 인증 처리 합니다.
        if (StringUtils.hasText(token) && jwtProvider.validateTokenExceptExpiration(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

