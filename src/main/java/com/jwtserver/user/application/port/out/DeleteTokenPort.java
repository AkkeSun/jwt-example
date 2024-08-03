package com.jwtserver.user.application.port.out;

public interface DeleteTokenPort {

    void deleteToken(String username, String userAgent);
}
