package com.jwtserver.user.application.service.make_token;

import com.jwtserver.global.security.JwtAuthenticationProvider;
import com.jwtserver.user.adapter.out.redis.dto.RedisToken;
import com.jwtserver.user.application.port.in.MakeTokenUseCase;
import com.jwtserver.user.application.port.in.command.MakeTokenCommand;
import com.jwtserver.user.application.port.out.DeleteTokenPort;
import com.jwtserver.user.application.port.out.FindUserPort;
import com.jwtserver.user.application.port.out.RegisterTokenRedisPort;
import com.jwtserver.user.domain.UserDomain;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class MakeTokenService implements MakeTokenUseCase {

    private final FindUserPort findUserPort;
    private final RegisterTokenRedisPort registerTokenRedisPort;
    private final DeleteTokenPort deleteTokenPort;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final HttpServletRequest request;

    @Override
    public MakeTokenServiceResponse makeAccessToken(MakeTokenCommand command) {
        UserDomain user = findUserPort.findByUsernameAndPassword(command);
        String username = user.getUsername();
        String userAgent = request.getHeader("User-Agent");

        String accessToken = jwtAuthenticationProvider.createAccessToken(user);
        String refreshToken = jwtAuthenticationProvider.createRefreshToken(username);

        deleteTokenPort.deleteToken(user.getUsername(), userAgent);
        registerTokenRedisPort.registerRefreshToken(
            user.getUsername(), userAgent, new RedisToken().of(user, refreshToken));

        return MakeTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
