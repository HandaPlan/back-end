package com.org.candoit.domain.auth.controller;

import com.org.candoit.domain.auth.dto.LoginRequest;
import com.org.candoit.domain.auth.dto.LoginResponse;
import com.org.candoit.domain.auth.service.AuthService;
import com.org.candoit.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);

        return ResponseEntity.ok()
            .headers(loginResponse.getHttpHeaders())
            .body(ApiResponse.successWithoutData());

    }
}
