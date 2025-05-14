package com.org.candoit.global.security.jwt;

import com.org.candoit.global.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "01000", "유효하지 않은 토큰 입니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "01001", "만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "01002", "만료된 리프레시 토큰입니다."),
    ACCESS_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED,"01003","액세스 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED,"01004","리프레시 토큰이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
