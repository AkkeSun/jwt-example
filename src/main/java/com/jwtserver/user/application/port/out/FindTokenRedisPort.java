package com.jwtserver.user.application.port.out;

import com.jwtserver.user.adapter.out.redis.dto.RedisToken;

public interface FindTokenRedisPort {

    RedisToken findRefreshToken(String username, String userAgent);
}
