package com.org.candoit.global.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

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
