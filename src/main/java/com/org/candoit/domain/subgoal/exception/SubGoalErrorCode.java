package com.org.candoit.domain.subgoal.exception;

import com.org.candoit.global.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SubGoalErrorCode implements ErrorCode {
    NOT_FOUND_SUB_GOAL(HttpStatus.NOT_FOUND, "80000", "일치하는 서브 골을 찾지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
