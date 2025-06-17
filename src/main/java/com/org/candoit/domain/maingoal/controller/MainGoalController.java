package com.org.candoit.domain.maingoal.controller;

import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.CreateMainGoalResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalListResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalResponse;
import com.org.candoit.domain.maingoal.dto.UpdateMainGoalRequest;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.service.MainGoalService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main-goals")
public class MainGoalController {

    private final MainGoalService mainGoalService;

    @GetMapping
    public ResponseEntity<ApiResponse<MainGoalListResponse>> getMainGoals(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestParam(defaultValue = "all") String state
    ) {
        MainGoalListResponse result = mainGoalService.getPreviewList(member, checkFiltering(state));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private MainGoalStatus checkFiltering(String state) {
        if(state.equalsIgnoreCase("all")) return null;
        return MainGoalStatus.valueOf(state.toUpperCase());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateMainGoalResponse>> createMainGoal(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestBody CreateMainGoalRequest createMainGoalRequest) {
        CreateMainGoalResponse createMainGoalResponse = mainGoalService.createMainGoal(member,
            createMainGoalRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(createMainGoalResponse));
    }

    @DeleteMapping("{mainGoalId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteMainGoal(
        @Parameter(hidden = true) @LoginMember Member member,
        @PathVariable Long mainGoalId) {
        Boolean result = mainGoalService.deleteMainGoal(member, mainGoalId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("{mainGoalId}/rep")
    public ResponseEntity<ApiResponse<Boolean>> updateMainGoalRep(
        @Parameter(hidden = true) @LoginMember Member member,
        @PathVariable Long mainGoalId) {
        Boolean result = mainGoalService.updateMainGoalRep(member, mainGoalId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("{mainGoalId}")
    public ResponseEntity<ApiResponse<MainGoalResponse>> updateMainGoal(
        @Parameter(hidden = true) @LoginMember Member member,
        @PathVariable Long mainGoalId, @RequestBody UpdateMainGoalRequest updateMainGoalRequest) {
        MainGoalResponse mainGoalResponse = mainGoalService.updateMainGoal(member, mainGoalId, updateMainGoalRequest);
        return ResponseEntity.ok(ApiResponse.success(mainGoalResponse));
    }
}
