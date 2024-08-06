package com.sweettracker.authserver.user.application.service.reissue_token;

import com.sweettracker.authserver.global.exception.CustomAuthenticationException;
import com.sweettracker.authserver.global.exception.ErrorCode;
import com.sweettracker.authserver.global.util.JwtUtil;
import com.sweettracker.authserver.user.adapter.out.redis.dto.RedisToken;
import com.sweettracker.authserver.user.application.port.in.ReIssueTokenUseCase;
import com.sweettracker.authserver.user.application.port.out.FindTokenPort;
import com.sweettracker.authserver.user.application.port.out.FindTokenRedisPort;
import com.sweettracker.authserver.user.application.port.out.FindUserPort;
import com.sweettracker.authserver.user.application.port.out.RegisterTokenPort;
import com.sweettracker.authserver.user.application.port.out.RegisterTokenRedisPort;
import com.sweettracker.authserver.user.application.port.out.UpdateTokenPort;
import com.sweettracker.authserver.user.domain.RefreshToken;
import com.sweettracker.authserver.user.domain.UserDomain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class ReIssueTokenService implements ReIssueTokenUseCase {

    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final RegisterTokenRedisPort registerTokenRedisPort;
    private final RegisterTokenPort registerTokenPort;
    private final FindTokenRedisPort findRedisCachePort;
    private final FindTokenPort findTokenPort;
    private final UpdateTokenPort updateTokenPort;
    private final FindUserPort findUserPort;

    @Override
    public ReIssueTokenServiceResponse reIssueToken(String refreshToken) {
        if (!jwtUtil.validateTokenExceptExpiration(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String userAgent = request.getHeader("User-Agent");
        RedisToken redisToken = findRedisCachePort.findRefreshToken(username, userAgent);

        if (redisToken.isCircuitBreakerResponse()) {
            return reIssueFromDatabase(username, userAgent, refreshToken);
        }

        if (redisToken.isEmpty() || redisToken.isDifferentRefreshToken(refreshToken)) {
            RefreshToken dbToken = findTokenPort.findRefreshToken(username, userAgent);
            if (dbToken.isDifferentRefreshToken(refreshToken)) {
                throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
        }

        UserDomain user = new UserDomain().of(redisToken);
        String newAccessToken = jwtUtil.createAccessToken(user);
        String newRefreshToken = jwtUtil.createRefreshToken(username);

        redisToken.updateRefreshToken(newRefreshToken);
        registerTokenRedisPort.registerRefreshToken(username, userAgent, redisToken);

        return new ReIssueTokenServiceResponse().of(newAccessToken, newRefreshToken);
    }

    private ReIssueTokenServiceResponse reIssueFromDatabase(String username, String userAgent,
        String refreshToken) {

        UserDomain user = findUserPort.findByUsername(username);
        String newAccessToken = jwtUtil.createAccessToken(user);
        String newRefreshToken = jwtUtil.createRefreshToken(username);

        RefreshToken dbToken = findTokenPort.findRefreshToken(username, userAgent);

        if (dbToken.isEmpty()) {
            registerTokenPort.registerRefreshToken(username, userAgent, newRefreshToken);
            return new ReIssueTokenServiceResponse().of(newAccessToken, newRefreshToken);
        }
        if (dbToken.isDifferentRefreshToken(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        dbToken.updateRefreshToken(newRefreshToken);
        updateTokenPort.updateRefreshToken(dbToken);

        return new ReIssueTokenServiceResponse().of(newAccessToken, newRefreshToken);
    }
}