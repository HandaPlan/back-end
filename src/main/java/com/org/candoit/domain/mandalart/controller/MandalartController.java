package com.org.candoit.domain.mandalart.controller;

import com.org.candoit.domain.mandalart.dto.MainGoalOverviewResponse;
import com.org.candoit.domain.mandalart.dto.SubGoalOverviewResponse;
import com.org.candoit.domain.mandalart.service.MandalartService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mandalart")
public class MandalartController {

    private final MandalartService mandalartService;

    @GetMapping("/main-goals")
    public ResponseEntity<MainGoalOverviewResponse> getMandalart(
        @Parameter(hidden = true) @LoginMember Member loginMember) {
        MainGoalOverviewResponse result = mandalartService.getMainGoalList(loginMember);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/main-goals/{mainGoalId}")
    public ResponseEntity<SubGoalOverviewResponse> getSubGoalList(
        @Parameter(hidden = true) @LoginMember Member loginMember, @PathVariable Long mainGoalId) {
        SubGoalOverviewResponse result = mandalartService.getSubGoalList(loginMember, mainGoalId);
        return ResponseEntity.ok(result);
    }
}
