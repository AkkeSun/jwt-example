package com.sweettracker.authserver.user.application.port.out;

import com.sweettracker.authserver.user.adapter.out.redis.dto.RedisToken;

public interface FindTokenRedisPort {

    RedisToken findRefreshToken(String username, String userAgent);
}
