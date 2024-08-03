package com.jwtserver.global.security;

import com.jwtserver.user.domain.UserDomain;
import org.springframework.security.core.Authentication;

public interface JwtAuthenticationProvider {

    String createAccessToken(UserDomain user);

    String createRefreshToken(String username);

    Authentication getAuthentication(String token);

    boolean validateTokenExceptExpiration(String token);

    String getUsername(String token);
}
