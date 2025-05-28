package com.org.candoit.global.security.jwt;

import com.org.candoit.domain.auth.dto.NewTokenResponse;
import com.org.candoit.domain.auth.exception.AuthErrorCode;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.response.GlobalErrorCode;
import com.org.candoit.global.security.basic.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public NewTokenResponse reissue(String accessToken, String refreshToken) {
        log.info("accessToken: {} refreshToken: {}", accessToken, refreshToken);
        String extractAccessToken = jwtUtil.removePrefixFromAccessToken(accessToken);

        Claims claimsAccessToken = jwtUtil.extractClaimsOrThrow("accessToken", extractAccessToken);
        Claims claimsRefreshToken = jwtUtil.extractClaimsOrThrow("refreshToken", refreshToken);

        String memberIdFromAccessToken = claimsAccessToken.getSubject();
        String memberIdFromRefreshToken = claimsRefreshToken.getSubject();

        if (!memberIdFromRefreshToken.equals(memberIdFromAccessToken)) {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }

        if (jwtUtil.checkBlacklist(extractAccessToken)) {
            logout(accessToken);
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }

        jwtUtil.validateAccessTokenExpiration(claimsAccessToken, extractAccessToken);

        String existingRefreshToken = refreshTokenRepository.findByMemberId(
            memberIdFromRefreshToken);
        if (existingRefreshToken == null) {
            throw new CustomException(AuthErrorCode.UNAUTHORIZED);
        }

        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(authentication);
        String newRefreshToken = jwtUtil.generateRefreshToken(authentication);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
            customUserDetails, null, customUserDetails.getAuthorities());

        log.info("jwtService: 로그인한 사용자: {}", ((CustomUserDetails)newAuthentication.getPrincipal()).getMember().getNickname());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return new NewTokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken) {

        String extractAccessToken = jwtUtil.removePrefixFromAccessToken(accessToken);
        Claims claimsAccessToken = jwtUtil.extractClaimsOrThrow("accessToken", extractAccessToken);

        jwtUtil.addBlackListExistingAccessToken(extractAccessToken,
            claimsAccessToken.getExpiration());

        if (!refreshTokenRepository.findByMemberId(claimsAccessToken.getSubject()).isEmpty()) {
            refreshTokenRepository.delete(claimsAccessToken.getSubject());
        }
    }


}
