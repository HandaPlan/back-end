package com.org.candoit.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    private String profile_image;
    private String comment;
    private String email;
    private String nickname;
}
