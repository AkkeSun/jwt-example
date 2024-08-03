package com.jwtserver.user.application.port.out;

public interface RegisterTokenPort {

    void registerRefreshToken(String username, String userAgent, String refreshToken);
}
