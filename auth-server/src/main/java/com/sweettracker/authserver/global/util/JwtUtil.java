package com.sweettracker.authserver.global.util;

import com.sweettracker.authserver.user.domain.UserDomain;

public interface JwtUtil {

    String createAccessToken(UserDomain user);

    String createRefreshToken(String username);

    boolean validateTokenExceptExpiration(String token);

    String getUsername(String token);

}
