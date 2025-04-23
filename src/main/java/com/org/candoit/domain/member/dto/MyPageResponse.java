package com.org.candoit.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPageResponse {

    private String profile_image;
    private String comment;
    private String email;
    private String nickname;
}
