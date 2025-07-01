package com.org.candoit.domain.auth.service;

import com.org.candoit.domain.auth.dto.LoginRequest;
import com.org.candoit.domain.auth.dto.LoginResponse;
import com.org.candoit.domain.auth.dto.LogoutResponse;
import com.org.candoit.domain.auth.dto.NewTokenResponse;
import com.org.candoit.domain.auth.dto.ReissueResponse;
import com.org.candoit.domain.member.dto.BasicMemberInfoResponse;
import com.org.candoit.global.security.basic.CustomUserDetails;
import com.org.candoit.global.security.jwt.JwtService;
import com.org.candoit.global.security.jwt.JwtUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getId(),
                    loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        accessTokenSend2Client(headers, accessToken);
        refreshTokenSend2Client(headers, refreshToken, 7);

        return new LoginResponse(headers,
            new BasicMemberInfoResponse((CustomUserDetails) authentication.getPrincipal()));
    }

    private void accessTokenSend2Client(HttpHeaders headers, String accessToken) {
        headers.set("Authorization", "Bearer " + accessToken);
    }

    private void refreshTokenSend2Client(HttpHeaders headers, String refreshToken, long duration) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(duration))
            .sameSite("None")
            .build();

        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public LogoutResponse logout(String accessToken, String refreshToken) {
        jwtService.logout(accessToken);

        HttpHeaders headers = new HttpHeaders();
        refreshTokenSend2Client(headers, refreshToken, 0);

        return new LogoutResponse(headers);
    }

    public ReissueResponse reissue(String refreshToken, String accessToken) {

        NewTokenResponse newTokenResponse = jwtService.reissue(refreshToken, accessToken);
        HttpHeaders headers = new HttpHeaders();
        accessTokenSend2Client(headers, newTokenResponse.getAccessToken());
        refreshTokenSend2Client(headers, newTokenResponse.getRefreshToken(), 7);

        return new ReissueResponse(headers);
    }
}
