package com.org.candoit.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    private String profile_image;
    private String comment;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String nickname;
}
