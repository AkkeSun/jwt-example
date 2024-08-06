package com.sweettracker.resourceserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // status code 401 (3001 - 3099) : Unauthorized
    INVALID_ACCESS_TOKEN(3001, "유효한 인증 토큰이 아닙니다"),

    // status code 403 (4001 - 4099) : Forbidden
    ACCESS_DENIED(4001, "접근 권한이 없습니다"),

    ;

    private final int code;
    private final String message;
}

