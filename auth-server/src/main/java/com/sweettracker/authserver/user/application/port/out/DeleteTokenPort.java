package com.sweettracker.authserver.user.application.port.out;

public interface DeleteTokenPort {

    void deleteToken(String username, String userAgent);
}
