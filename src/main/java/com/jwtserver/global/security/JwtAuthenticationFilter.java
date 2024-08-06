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

        try {
            if (StringUtils.hasText(token) && jwtProvider.validateTokenExceptExpiration(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ignored) {
            // 예외는 authenticationEntryPoint 에처 처리됩니다.
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

