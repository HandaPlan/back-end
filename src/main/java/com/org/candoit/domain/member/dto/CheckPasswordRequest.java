package com.org.candoit.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckPasswordRequest {
    @NotBlank
    private String type;
    private String password;
}
