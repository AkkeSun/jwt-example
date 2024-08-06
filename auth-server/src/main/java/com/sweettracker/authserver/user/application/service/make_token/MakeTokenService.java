package com.sweettracker.authserver.user.application.service.make_token;

import com.sweettracker.authserver.global.util.JwtUtil;
import com.sweettracker.authserver.user.adapter.out.redis.dto.RedisToken;
import com.sweettracker.authserver.user.application.port.in.MakeTokenUseCase;
import com.sweettracker.authserver.user.application.port.in.command.MakeTokenCommand;
import com.sweettracker.authserver.user.application.port.out.DeleteTokenPort;
import com.sweettracker.authserver.user.application.port.out.FindUserPort;
import com.sweettracker.authserver.user.application.port.out.RegisterTokenRedisPort;
import com.sweettracker.authserver.user.domain.UserDomain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class MakeTokenService implements MakeTokenUseCase {

    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final FindUserPort findUserPort;
    private final RegisterTokenRedisPort registerTokenRedisPort;
    private final DeleteTokenPort deleteTokenPort;

    @Override
    public MakeTokenServiceResponse makeAccessToken(MakeTokenCommand command) {
        UserDomain user = findUserPort.findByUsernameAndPassword(command);
        String username = user.getUsername();
        String userAgent = request.getHeader("User-Agent");

        String accessToken = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(username);

        deleteTokenPort.deleteToken(user.getUsername(), userAgent);
        registerTokenRedisPort.registerRefreshToken(
            user.getUsername(), userAgent, new RedisToken().of(user, refreshToken));

        return MakeTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
