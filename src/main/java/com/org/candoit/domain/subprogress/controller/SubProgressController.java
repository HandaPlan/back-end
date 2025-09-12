package com.org.candoit.domain.subprogress.controller;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subprogress.dto.CheckedSubProgressResponse;
import com.org.candoit.domain.subprogress.dto.DateUnit;
import com.org.candoit.domain.subprogress.dto.SubProgressOverviewResponse;
import com.org.candoit.domain.subprogress.service.SubProgressService;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.org.candoit.domain.subprogress.dto.Direction;

@RestController
@RequiredArgsConstructor
public class SubProgressController {

    private final SubProgressService subProgressService;

    @GetMapping("/api/main-goals/{mainGoalId}/sub-progress")
    public ResponseEntity<ApiResponse<SubProgressOverviewResponse>> getWeeklySubProgress(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long mainGoalId,
        @RequestParam LocalDate date, @RequestParam Direction direction) {
        SubProgressOverviewResponse result = subProgressService.getProgressWithoutSubGoal(
            loginMember, mainGoalId, date, direction);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/api/main-goals/{mainGoalId}/sub-progress/checked")
    public ResponseEntity<ApiResponse<List<CheckedSubProgressResponse>>> getCheckedSubProgress(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long mainGoalId,
        @RequestParam LocalDate date, @RequestParam Direction direction,
        @RequestParam DateUnit unit
    ) {
        List<CheckedSubProgressResponse> result = subProgressService.getCheckedProgress(
            loginMember, mainGoalId, date, direction, unit
        );
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
