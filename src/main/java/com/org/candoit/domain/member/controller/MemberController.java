package com.org.candoit.domain.member.controller;

import com.org.candoit.domain.member.dto.CheckPasswordRequest;
import com.org.candoit.domain.member.dto.MemberCheckRequest;
import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.dto.MemberUpdateRequest;
import com.org.candoit.domain.member.dto.MyPageResponse;
import com.org.candoit.domain.member.dto.NewPasswordRequest;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.service.MemberService;
import com.org.candoit.domain.member.service.ResetPasswordRequest;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPage(
        @Parameter(hidden = true) @LoginMember Member loginMember) {
        MyPageResponse myPageResponse = memberService.getMyPage(loginMember.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(myPageResponse));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Object>> join(
        @Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        memberService.join(memberJoinRequest);
        return ResponseEntity.ok(ApiResponse.successWithoutData());
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> check(
        @Valid @RequestBody MemberCheckRequest memberCheckRequest) {
        Boolean result = memberService.check(memberCheckRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<MyPageResponse>> updateMemberInfo(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        MyPageResponse myPageResponse = memberService.updateInfo(loginMember.getMemberId(),
            memberUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(myPageResponse));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> withdraw(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestBody CheckPasswordRequest checkPasswordRequest) {
        memberService.withdraw(loginMember.getMemberId(), checkPasswordRequest);
        return ResponseEntity.ok(ApiResponse.successWithoutData());
    }

    @PostMapping("password-check")
    public ResponseEntity<ApiResponse<Object>> checkCorrectPassword(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestBody CheckPasswordRequest checkPasswordRequest) {
        memberService.checkPassword(loginMember, checkPasswordRequest);
        return ResponseEntity.ok(ApiResponse.successWithoutData());
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> updatePassword(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestBody NewPasswordRequest newPasswordRequest) {
        memberService.updatePassword(loginMember.getMemberId(), newPasswordRequest);
        return ResponseEntity.ok(ApiResponse.successWithoutData());
    }

    @PostMapping("/new-password")
    public ResponseEntity<ApiResponse<Boolean>> newPassword(
        @RequestBody ResetPasswordRequest resetPasswordRequest) {
        Boolean result = memberService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
