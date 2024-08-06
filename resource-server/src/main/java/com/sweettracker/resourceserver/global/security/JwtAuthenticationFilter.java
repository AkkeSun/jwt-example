package com.sweettracker.resourceserver.global.security;

import com.sweettracker.resourceserver.global.exception.CustomAuthenticationException;
import com.sweettracker.resourceserver.global.exception.ErrorCode;
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
            Claims jwt = parseJwtToken(token);
            if (!jwt.getExpiration().before(new Date())) {
                SecurityContextHolder.getContext().setAuthentication(makeAuthenticationToken(jwt));
            }

        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private Claims parseJwtToken(String token) {
        try {
            token = token.replace("Bearer ", "");
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    private Authentication makeAuthenticationToken(Claims claims) {
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
            List.of(new SimpleGrantedAuthority((String) claims.get("roles"))));
    }
}

