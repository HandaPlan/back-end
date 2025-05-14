package com.org.candoit.domain.auth.exception;

import com.org.candoit.global.response.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"14000", "로그인된 사용자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
