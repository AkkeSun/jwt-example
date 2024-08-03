package com.jwtserver.user.application.port.in;

import com.jwtserver.user.application.service.reissue_token.ReIssueTokenServiceResponse;

public interface ReIssueTokenUseCase {

    ReIssueTokenServiceResponse reIssueToken(String refreshToken);
}
