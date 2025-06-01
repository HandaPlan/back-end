package com.org.candoit.domain.maingoal.controller;

import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.MainGoalResponse;
import com.org.candoit.domain.maingoal.service.MainGoalService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main-goals")
public class MainGoalController {

    private final MainGoalService mainGoalService;

    @PostMapping
    public ResponseEntity<ApiResponse<MainGoalResponse>> createMainGoal(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestBody CreateMainGoalRequest createMainGoalRequest) {
        MainGoalResponse mainGoalResponse = mainGoalService.createMainGoal(member,
            createMainGoalRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(mainGoalResponse));
    }

    @DeleteMapping("{mainGoalId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteMainGoal(
        @Parameter(hidden = true) @LoginMember Member member,
        @PathVariable Long mainGoalId) {
        Boolean result = mainGoalService.deleteMainGoal(member, mainGoalId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
