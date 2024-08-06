package com.sweettracker.authserver.user.adapter.out.redis;

import com.sweettracker.authserver.global.util.JsonUtil;
import com.sweettracker.authserver.user.adapter.out.redis.dto.RedisToken;
import com.sweettracker.authserver.user.application.port.out.FindTokenRedisPort;
import com.sweettracker.authserver.user.application.port.out.RegisterTokenRedisPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
class RedisAdapter implements FindTokenRedisPort, RegisterTokenRedisPort {

    @Value("${jwt.token.refresh-valid-time}")
    private long validTime;

    private final JsonUtil jsonUtil;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findRefreshTokenFallback")
    public RedisToken findRefreshToken(String username, String userAgent) {
        String redisKey = username + "-" + userAgent + "::token";
        String redisData = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.hasText(redisData)) {
            return jsonUtil.parseJson(redisData, RedisToken.class);
        }
        return RedisToken.builder().refreshToken("empty").build();
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "registerRefreshTokenFallback")
    public void registerRefreshToken(String username, String userAgent,
        RedisToken redisToken) {
        String redisKey = username + "-" + userAgent + "::token";
        String redisData = jsonUtil.toJsonString(redisToken);
        redisTemplate.opsForValue().set(redisKey, redisData, validTime, TimeUnit.MILLISECONDS);
    }

    private RedisToken findRefreshTokenFallback(String username, String userAgent, Exception e) {
        log.error("[findRefreshTokenFallback] call - " + e.getMessage());
        return RedisToken.builder().refreshToken("redisError").build();
    }

    private void registerRefreshTokenFallback(String username, String userAgent,
        RedisToken refreshTokenCache, Exception e) {
        log.error("[findRefreshTokenFallback] call");
    }
}
