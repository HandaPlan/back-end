package com.org.candoit.domain.dailyprogress.controller;

import com.org.candoit.domain.dailyprogress.dto.CheckDailyProgressRequest;
import com.org.candoit.domain.dailyprogress.service.DailyProgressService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DailyProgressController {

    private final DailyProgressService dailyProgressService;

    @PostMapping("/daily-actions/{dailyActionId}/daily-progress")
    public ResponseEntity<ApiResponse<Boolean>> checkedDate(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long dailyActionId,
        @RequestBody CheckDailyProgressRequest checkDailyProgressRequest
    ){

        Boolean result = dailyProgressService.checkedDate(loginMember, dailyActionId, checkDailyProgressRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
