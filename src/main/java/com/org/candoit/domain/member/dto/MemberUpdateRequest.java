package com.org.candoit.domain.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    private String profile_image;
    private String comment;
    @Email
    private String email;
    private String nickname;
}
