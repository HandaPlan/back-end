package com.org.candoit.global.security.jwt;

import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.security.basic.CustomUserDetails;
import com.org.candoit.global.security.basic.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Key key;
    private final RedisTemplate<String, String> redisTemplate;

    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access-token-expiration}") Long accessTokenExpiration,
        @Value("${jwt.refresh-token-expiration}") Long refreshTokenExpiration,
        RefreshTokenRepository refreshTokenRepository,
        CustomUserDetailsService customUserDetailsService,
        RedisTemplate<String, String> redisTemplate) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails nowMember = (CustomUserDetails) authentication.getPrincipal();

        Instant nowTime = Instant.now();

        return Jwts.builder()
            .subject(nowMember.getMember().getMemberId().toString())
            .claim("status", nowMember.getMember().getMemberStatus().name())
            .claim("auth", nowMember.getMember().getMemberRole().name())
            .issuedAt(Date.from(nowTime))
            .expiration(Date.from(nowTime.plus(accessTokenExpiration, ChronoUnit.MILLIS)))
            .signWith(key)
            .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails nowMember = (CustomUserDetails) authentication.getPrincipal();

        Instant nowTime = Instant.now();
        String refreshToken = Jwts.builder()
            .subject(nowMember.getMember().getMemberId().toString())
            .issuedAt(Date.from(nowTime))
            .expiration(Date.from(nowTime.plus(refreshTokenExpiration, ChronoUnit.MILLIS)))
            .signWith(key)
            .compact();

        refreshTokenRepository.save(nowMember.getMember().getMemberId(), refreshToken);

        return refreshToken;
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public Claims extractClaimsOrThrow(String type, String token) {

        try {
            return parseToken(token);
        } catch (ExpiredJwtException e) {
            if (type.equals("accessToken")) {
                return e.getClaims();
            }
            throw new CustomException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (JwtException e) {
            throw new CustomException(TokenErrorCode.INVALID_TOKEN);
        }
    }

    public String removePrefixFromAccessToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new CustomException(TokenErrorCode.ACCESS_TOKEN_NOT_EXIST);
        }
        return bearerToken.substring(7);
    }

    public boolean checkBlacklist(String accessToken) {
        return redisTemplate.hasKey("blacklist:access-token:" + accessToken);
    }

    public void addBlackListExistingAccessToken(String accessToken, Date expirationDate) {

        redisTemplate.opsForValue()
            .set("blacklist:access-token:" + accessToken, expirationDate.toString(),
                Duration.between(new Date().toInstant(), expirationDate.toInstant()).getSeconds(),
                TimeUnit.SECONDS);
    }

    public Authentication getAuthentication(String token) {

        String memberId = parseToken(token).getSubject();

        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(String.valueOf(memberId));
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }
}
