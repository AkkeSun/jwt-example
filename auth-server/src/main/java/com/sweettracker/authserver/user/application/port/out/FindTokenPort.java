package com.sweettracker.authserver.user.application.port.out;

import com.sweettracker.authserver.user.domain.RefreshToken;

public interface FindTokenPort {

    RefreshToken findRefreshToken(String username, String userAgent);
}
