package com.org.candoit.domain.auth.exception;

import com.org.candoit.global.response.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {

    NOT_MATCH_AUTH_CODE(HttpStatus.BAD_REQUEST, "14001","인증 코드가 일치하지 않습니다."),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED,"14002", "인증 코드가 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"14000", "로그인된 사용자가 아닙니다."),
    EMAIL_VERIFICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "14003", "이메일 인증이 필요합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
