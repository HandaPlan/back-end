package com.org.candoit.global.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"10000", "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
