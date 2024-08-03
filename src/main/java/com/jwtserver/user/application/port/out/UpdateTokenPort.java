package com.jwtserver.user.application.port.out;

import com.jwtserver.user.domain.RefreshToken;

public interface UpdateTokenPort {

    void updateRefreshToken(RefreshToken refreshToken);
}
