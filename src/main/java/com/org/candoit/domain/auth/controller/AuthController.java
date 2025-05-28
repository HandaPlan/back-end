package com.org.candoit.domain.auth.controller;

import com.org.candoit.domain.auth.dto.LoginRequest;
import com.org.candoit.domain.auth.dto.LoginResponse;
import com.org.candoit.domain.auth.dto.LogoutResponse;
import com.org.candoit.domain.auth.dto.ReissueResponse;
import com.org.candoit.domain.auth.service.AuthService;
import com.org.candoit.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
        @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
        @Parameter(hidden = true) @CookieValue(name = "refresh-token") String refreshToken
    ) {
        LogoutResponse logoutResponse = authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().headers(logoutResponse.getHttpHeaders())
            .body(ApiResponse.successWithoutData());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<Object>> reissue(
        @RequestHeader("Authorization") String accessToken,
        @Parameter(hidden = true) @CookieValue(name = "refresh-token") String refreshToken) {
        ReissueResponse reissueResponse = authService.reissue(accessToken, refreshToken);
        return ResponseEntity.ok().headers(reissueResponse.getHttpHeaders())
            .body(ApiResponse.successWithoutData());
    }
}
