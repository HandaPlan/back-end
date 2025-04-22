package com.org.candoit.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberJoinRequest {

    private String email;
    private String password;
    private String nickname;
}
