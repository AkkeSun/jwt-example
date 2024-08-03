package com.jwtserver.user.application.port.in;

import com.jwtserver.user.application.service.find_user_info.FindUserInfoServiceResponse;

public interface FindUserInfoUseCase {

    FindUserInfoServiceResponse findUserInfo(String accessToken);
}
