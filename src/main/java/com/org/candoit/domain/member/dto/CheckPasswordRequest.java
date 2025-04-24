package com.org.candoit.domain.member.dto;

import lombok.Getter;

@Getter
public class CheckPasswordRequest {
    private String type;
    private String password;
}
