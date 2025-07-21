package com.org.candoit.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberJoinRequest {

    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
}
