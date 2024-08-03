package com.jwtserver.user.adapter.out.persistence;

import com.jwtserver.user.domain.RefreshToken;
import org.springframework.stereotype.Component;

@Component
class TokenMapper {

    RefreshToken toDomain(UserRefreshTokenEntity entity) {
        return RefreshToken.builder()
            .username(entity.getUsername())
            .userAgent(entity.getUserAgent())
            .refreshToken(entity.getRefreshToken())
            .build();
    }
}
