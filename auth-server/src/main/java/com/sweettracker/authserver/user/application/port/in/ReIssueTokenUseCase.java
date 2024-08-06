package com.sweettracker.authserver.user.application.port.in;

import com.sweettracker.authserver.user.application.service.reissue_token.ReIssueTokenServiceResponse;

public interface ReIssueTokenUseCase {

    ReIssueTokenServiceResponse reIssueToken(String refreshToken);
}
