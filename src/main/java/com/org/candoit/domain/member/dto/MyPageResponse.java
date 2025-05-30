package com.org.candoit.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPageResponse {

    private String profileImage;
    private String comment;
    private String email;
    private String nickname;
}
