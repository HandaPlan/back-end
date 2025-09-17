package com.org.candoit.domain.home.controller;

import com.org.candoit.domain.home.dto.HomeOverallResponse;
import com.org.candoit.domain.home.service.HomeService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<ApiResponse<HomeOverallResponse>> getHome(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestParam(required = false) Long mainGoalId
    ) {
        HomeOverallResponse result = homeService.getHomeOverall(member, mainGoalId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
