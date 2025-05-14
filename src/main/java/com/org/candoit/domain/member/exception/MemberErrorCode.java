package com.org.candoit.domain.member.exception;

import com.org.candoit.global.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "00000", "사용자를 찾지 못했습니다."),
    NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST, "00001", "비밀번호가 일치하지 않습니다."),
    WITHDRAWN_MEMBER(HttpStatus.BAD_REQUEST, "00002", "탈퇴한 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
