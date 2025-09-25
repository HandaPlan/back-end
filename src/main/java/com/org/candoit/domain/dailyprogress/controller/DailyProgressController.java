package com.org.candoit.domain.dailyprogress.controller;

import com.org.candoit.domain.dailyaction.dto.ActionDates;
import com.org.candoit.domain.dailyprogress.dto.DailyProgressResponse;
import com.org.candoit.domain.dailyprogress.dto.DetailProgressResponse;
import com.org.candoit.domain.dailyprogress.service.DailyProgressService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subprogress.dto.DateUnit;
import com.org.candoit.domain.subprogress.dto.Direction;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DailyProgressController {

    private final DailyProgressService dailyProgressService;

    @PutMapping("/sub-goals/{subGoalId}/daily-progress")
    public ResponseEntity<ApiResponse<List<DetailProgressResponse>>> checkedDate(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId,
        @RequestBody List<ActionDates> checkDailyProgressRequest
    ) {
        List<DetailProgressResponse> result = dailyProgressService.checkedDate(loginMember,
            subGoalId, checkDailyProgressRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/sub-goals/{subGoalId}/daily-progress")
    public ResponseEntity<ApiResponse<List<DetailProgressResponse>>> getDailyProgress(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId
    ) {
        List<DetailProgressResponse> result = dailyProgressService.getDailyProgress(loginMember,
            subGoalId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/sub-goals/{subGoalId}/daily-progress/checked")
    public ResponseEntity<ApiResponse<DailyProgressResponse>> getDailyProgressCalendar(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long subGoalId, @RequestParam LocalDate date,
        @RequestParam Direction direction, @RequestParam DateUnit unit
    ) {
        DailyProgressResponse result = dailyProgressService.getCalendarWithoutDailyActions(
            loginMember, subGoalId, date, direction, unit);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
