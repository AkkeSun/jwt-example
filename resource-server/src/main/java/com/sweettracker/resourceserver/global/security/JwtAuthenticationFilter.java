package com.sweettracker.resourceserver.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/*
	API 를 호출할 때 사용되는 인증필터 입니다.
*/
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        try {
            token = token.replace("Bearer ", "");
            Claims jwt = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            if (!jwt.getExpiration().before(new Date())) {
                SecurityContextHolder.getContext().setAuthentication(makeAuthenticationToken(jwt));
            }

        } catch (Exception ignored) {
            // 예외는 authenticationEntryPoint 에서 처리합니다.
        }

        filterChain.doFilter(request, response);
    }

    private Authentication makeAuthenticationToken(Claims claims) {
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
            List.of(new SimpleGrantedAuthority((String) claims.get("roles"))));
    }
}

