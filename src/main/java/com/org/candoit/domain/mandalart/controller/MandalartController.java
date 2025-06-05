package com.org.candoit.domain.mandalart.controller;

import com.org.candoit.domain.mandalart.dto.DetailMandalartResponse;
import com.org.candoit.domain.mandalart.service.MandalartService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mandalart")
public class MandalartController {

    private final MandalartService mandalartService;

    @GetMapping
    public ResponseEntity<DetailMandalartResponse> getMandalart(@Parameter(hidden = true) @LoginMember Member loginMember) {
        DetailMandalartResponse result = mandalartService.getMandalart(loginMember);
        return ResponseEntity.ok(result);
    }
}
