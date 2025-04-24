package com.org.candoit.domain.member.controller;

import com.org.candoit.domain.member.dto.MemberCheckRequest;
import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.dto.MemberUpdateRequest;
import com.org.candoit.domain.member.dto.MyPageResponse;
import com.org.candoit.domain.member.service.MemberService;
import com.org.candoit.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPage(){
        MyPageResponse myPageResponse = memberService.getMyPage(4l);
        return ResponseEntity.ok(ApiResponse.success(myPageResponse));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Object>> join(@RequestBody MemberJoinRequest memberJoinRequest){
        memberService.join(memberJoinRequest);
        return ResponseEntity.ok(ApiResponse.successWithoutData());
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> check(@RequestBody MemberCheckRequest memberCheckRequest){
        Boolean result = memberService.check(memberCheckRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<MyPageResponse>> updateMemberInfo(@RequestBody MemberUpdateRequest memberUpdateRequest){
        MyPageResponse myPageResponse = memberService.updateInfo(4l, memberUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(myPageResponse));
    }
}
