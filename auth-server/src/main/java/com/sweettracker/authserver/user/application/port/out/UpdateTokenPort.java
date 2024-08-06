package com.sweettracker.authserver.user.application.port.out;

import com.sweettracker.authserver.user.domain.RefreshToken;

public interface UpdateTokenPort {

    void updateRefreshToken(RefreshToken refreshToken);
}
