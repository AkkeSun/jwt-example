package com.jwtserver.global.security;

import com.jwtserver.global.exception.CustomAuthenticationException;
import com.jwtserver.global.exception.ErrorCode;
import com.jwtserver.user.application.port.out.FindTokenPort;
import com.jwtserver.user.domain.UserDomain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProviderImpl implements JwtAuthenticationProvider {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Value("${jwt.token.valid-time}")
    private long tokenValidTime;

    @Value("${jwt.token.refresh-valid-time}")
    private long refreshTokenValidTime;

    private final UserDetailsService userDetailsService;

    private final FindTokenPort findTokenPort;

    @Override
    public String createAccessToken(UserDomain user) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("roles", user.getRole());
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    @Override
    public String createRefreshToken(String username) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(username);
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    @Override
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
            List.of(new SimpleGrantedAuthority((String) claims.get("roles"))));
    }

    public Claims getClaims(String token) {
        try {
            token = token.replace("Bearer ", "");
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("[getTokenInfo] token: {}, - {}", token, e.getMessage());
            throw new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}
