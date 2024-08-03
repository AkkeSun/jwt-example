package com.jwtserver.user.application.port.out;

import com.jwtserver.user.adapter.out.redis.dto.RedisToken;

public interface RegisterTokenRedisPort {

    void registerRefreshToken(String username, String userAgent,
        RedisToken refreshToken);
}
