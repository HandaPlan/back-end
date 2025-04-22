package com.org.candoit.domain.member.controller;

import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.service.MemberService;
import com.org.candoit.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping("/join")
    public ResponseEntity<ApiResponse<Object>> join(@RequestBody MemberJoinRequest memberJoinRequest){
        memberService.join(memberJoinRequest);
        return ResponseEntity.ok(ApiResponse.successWithoutData());
    }
}
