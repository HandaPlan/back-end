package com.org.candoit.domain.maingoal.exception;

import com.org.candoit.global.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MainGoalErrorCode implements ErrorCode {
    NOT_FOUND_MAIN_GOAL(HttpStatus.NOT_FOUND, "20000", "일치하는 메인 골을 찾지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
