package com.org.candoit.global.s3.controller;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import com.org.candoit.global.s3.dto.PresignedUrlResponse;
import com.org.candoit.global.s3.service.S3PresignService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class S3Controller {

    private final S3PresignService s3PresignService;


    @PostMapping("presigned")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> profilePresigned(
        @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        PresignedUrlResponse result = s3PresignService.createUploadUrl(
            String.valueOf(loginMember.getMemberId()));
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
