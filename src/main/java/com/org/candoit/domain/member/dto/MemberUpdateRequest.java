package com.org.candoit.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    private String profileImage;
    private String comment;
    private String nickname;
}
