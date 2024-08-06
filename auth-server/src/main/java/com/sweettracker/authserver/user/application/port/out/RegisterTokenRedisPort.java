package com.sweettracker.authserver.user.application.port.out;

import com.sweettracker.authserver.user.adapter.out.redis.dto.RedisToken;

public interface RegisterTokenRedisPort {

    void registerRefreshToken(String username, String userAgent,
        RedisToken refreshToken);
}
