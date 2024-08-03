package com.jwtserver.user.application.port.out;

import com.jwtserver.user.domain.RefreshToken;

public interface FindTokenPort {

    RefreshToken findRefreshToken(String username, String userAgent);
}
