package com.sweettracker.authserver.user.application.port.in;

import com.sweettracker.authserver.user.application.service.find_user_info.FindUserInfoServiceResponse;

public interface FindUserInfoUseCase {

    FindUserInfoServiceResponse findUserInfo(String accessToken);
}
