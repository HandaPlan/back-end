package com.org.candoit.domain.dailyaction.exception;

import com.org.candoit.global.response.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DailyActionErrorCode implements ErrorCode {

    NOT_FOUND_DAILY_ACTION(HttpStatus.NOT_FOUND,"18000", "일치하는 데일리 액션을 찾지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
