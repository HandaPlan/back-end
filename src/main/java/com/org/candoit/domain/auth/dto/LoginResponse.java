package com.org.candoit.domain.auth.dto;


import com.org.candoit.domain.member.dto.BasicMemberInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponse {

    private HttpHeaders httpHeaders;
    private BasicMemberInfoResponse memberInfo;
}
