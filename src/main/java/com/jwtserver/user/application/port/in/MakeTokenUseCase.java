package com.jwtserver.user.application.port.in;

import com.jwtserver.user.application.port.in.command.MakeTokenCommand;
import com.jwtserver.user.application.service.make_token.MakeTokenServiceResponse;

public interface MakeTokenUseCase {

    MakeTokenServiceResponse makeAccessToken(MakeTokenCommand command);
}
