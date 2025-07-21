package com.org.candoit.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberCheckRequest {
    @NotBlank
    String type;
    String content;
}
