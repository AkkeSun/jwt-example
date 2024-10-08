package com.sweettracker.resourceserver.global.exception;

import com.sweettracker.resourceserver.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityExceptionController {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @GetMapping("/errors/access-denied")
    public ApiResponse<Object> CustomAuthorizationException() {
        return ApiResponse.of(
            HttpStatus.FORBIDDEN,
            ErrorResponse.builder()
                .errorCode(ErrorCode.ACCESS_DENIED.getCode())
                .errorMessage(ErrorCode.ACCESS_DENIED.getMessage())
                .build()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @GetMapping("/errors/invalid-token")
    public ApiResponse<Object> CustomAuthenticationException() {
        return ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_ACCESS_TOKEN.getCode())
                .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN.getMessage())
                .build()
        );
    }
}
