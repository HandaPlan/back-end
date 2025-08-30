package com.org.candoit.domain.dailyaction.controller;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.service.DailyActionService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class DailyActionController {

    private final DailyActionService dailyActionService;

    @PostMapping("/sub-goals/{subGoalId}/daily-actions")
    public ResponseEntity<ApiResponse<DailyActionInfoWithAttainmentResponse>> createDailyAction(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId,
        @Valid @RequestBody CreateDailyActionRequest dailyActionRequest) {

        DailyActionInfoWithAttainmentResponse result = dailyActionService.createDailyAction(loginMember, subGoalId, dailyActionRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
