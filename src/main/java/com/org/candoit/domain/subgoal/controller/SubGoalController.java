package com.org.candoit.domain.subgoal.controller;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.DetailSubGoalResponse;
import com.org.candoit.domain.subgoal.dto.SimpleInfoWithAttainmentResponse;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.dto.UpdateSubGoalRequest;
import com.org.candoit.domain.subgoal.service.SubGoalService;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubGoalController {

    private final SubGoalService subGoalService;

    @PostMapping("/main-goals/{mainGoalId}/sub-goals")
    public ResponseEntity<ApiResponse<SimpleSubGoalInfoResponse>> createSubGoal(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long mainGoalId,
        @Valid @RequestBody CreateSubGoalRequest createSubGoalRequest) {

        SimpleSubGoalInfoResponse result = subGoalService.createSubGoal(loginMember, mainGoalId,
            createSubGoalRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("/sub-goals/{subGoalId}")
    public ResponseEntity<ApiResponse<SimpleInfoWithAttainmentResponse>> updateSubGoal(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId,
        @Valid @RequestBody UpdateSubGoalRequest updateSubGoalRequest) {

        SimpleInfoWithAttainmentResponse result = subGoalService.updateSubGoal(loginMember,
            subGoalId, updateSubGoalRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/sub-goals/{subGoalId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSubGoal(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId
    ) {

        Boolean result = subGoalService.deleteSubGoal(loginMember, subGoalId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/sub-goals/{subGoalId}")
    public ResponseEntity<ApiResponse<DetailSubGoalResponse>> getSubGoal(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId, @RequestParam String period
    ) {
        DetailSubGoalResponse result = subGoalService.getDetailSubGoal(loginMember, subGoalId,
            period);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
