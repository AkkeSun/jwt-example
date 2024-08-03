package com.jwtserver.user.application.service.reissue_token;

import com.jwtserver.global.exception.CustomAuthenticationException;
import com.jwtserver.global.exception.ErrorCode;
import com.jwtserver.global.security.JwtAuthenticationProvider;
import com.jwtserver.user.adapter.out.redis.dto.RedisToken;
import com.jwtserver.user.application.port.in.ReIssueTokenUseCase;
import com.jwtserver.user.application.port.out.FindTokenPort;
import com.jwtserver.user.application.port.out.FindTokenRedisPort;
import com.jwtserver.user.application.port.out.FindUserPort;
import com.jwtserver.user.application.port.out.RegisterTokenPort;
import com.jwtserver.user.application.port.out.RegisterTokenRedisPort;
import com.jwtserver.user.application.port.out.RegisterUserPort;
import com.jwtserver.user.application.port.out.UpdateTokenPort;
import com.jwtserver.user.domain.RefreshToken;
import com.jwtserver.user.domain.UserDomain;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class ReIssueTokenService implements ReIssueTokenUseCase {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final HttpServletRequest request;
    private final RegisterTokenRedisPort registerTokenRedisPort;
    private final RegisterTokenPort registerTokenPort;
    private final FindTokenRedisPort findRedisCachePort;
    private final FindTokenPort findTokenPort;
    private final UpdateTokenPort updateTokenPort;
    private final FindUserPort findUserPort;
    private final RegisterUserPort registerUserPort;

    @Override
    public ReIssueTokenServiceResponse reIssueToken(String refreshToken) {
        if (!jwtAuthenticationProvider.validateTokenExceptExpiration(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtAuthenticationProvider.getUsername(refreshToken);
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
        String newAccessToken = jwtAuthenticationProvider.createAccessToken(user);
        String newRefreshToken = jwtAuthenticationProvider.createRefreshToken(username);

        redisToken.updateRefreshToken(newRefreshToken);
        registerTokenRedisPort.registerRefreshToken(username, userAgent, redisToken);

        return new ReIssueTokenServiceResponse().of(newAccessToken, newRefreshToken);
    }

    private ReIssueTokenServiceResponse reIssueFromDatabase(String username, String userAgent,
        String refreshToken) {

        UserDomain user = findUserPort.findByUsername(username);
        String newAccessToken = jwtAuthenticationProvider.createAccessToken(user);
        String newRefreshToken = jwtAuthenticationProvider.createRefreshToken(username);

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