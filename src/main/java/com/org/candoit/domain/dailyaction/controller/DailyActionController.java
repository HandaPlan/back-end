package com.org.candoit.domain.dailyaction.controller;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyaction.dto.UpdateDailyActionRequest;
import com.org.candoit.domain.dailyaction.service.DailyActionService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<ApiResponse<SimpleDailyActionInfoResponse>> createDailyAction(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId,
        @Valid @RequestBody CreateDailyActionRequest dailyActionRequest) {

        SimpleDailyActionInfoResponse result = dailyActionService.createDailyAction(loginMember, subGoalId, dailyActionRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("/daily-actions/{dailyActionId}")
    public ResponseEntity<ApiResponse<DailyActionInfoWithAttainmentResponse>> updateDailyAction(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long dailyActionId,
        @Valid @RequestBody UpdateDailyActionRequest updateDailyActionRequest
    ){
        DailyActionInfoWithAttainmentResponse result = dailyActionService.updateDailyAction(loginMember, dailyActionId, updateDailyActionRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/daily-actions/{dailyActionId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteDailyAction(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long dailyActionId
    ){
        return ResponseEntity.ok(ApiResponse.success(dailyActionService.deleteDailyAction(loginMember, dailyActionId)));
    }
}
