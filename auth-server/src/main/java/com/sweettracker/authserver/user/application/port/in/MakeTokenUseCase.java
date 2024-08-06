package com.sweettracker.authserver.user.application.port.in;

import com.sweettracker.authserver.user.application.port.in.command.MakeTokenCommand;
import com.sweettracker.authserver.user.application.service.make_token.MakeTokenServiceResponse;

public interface MakeTokenUseCase {

    MakeTokenServiceResponse makeAccessToken(MakeTokenCommand command);
}
