package com.org.candoit.global.security.filter;

import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.security.basic.CustomUserDetails;
import com.org.candoit.global.security.jwt.JwtUtil;
import com.org.candoit.global.security.jwt.TokenErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String EXCEPTION_ATTRIBUTE = "exception";
    private final JwtUtil jwtUtil;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Set<String> excludeAllPaths = Set.of(
        "/swagger-ui/**", "/v3/api-docs/**",
        "/api/auth/login/**", "/api/members/join/**", "/api/auth/reissue/**",
        "/api/members/check"
    );

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (isExcludedPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;

        try {
            accessToken = jwtUtil.removePrefixFromAccessToken(request.getHeader("Authorization"));
            jwtUtil.parseToken(accessToken);
        } catch (CustomException e) {
            if (e.getErrorCode() == TokenErrorCode.ACCESS_TOKEN_NOT_EXIST) {
                request.setAttribute(EXCEPTION_ATTRIBUTE, "ACCESS_TOKEN_NOT_EXIST");
                throw new CustomException(TokenErrorCode.ACCESS_TOKEN_NOT_EXIST);
            }
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            request.setAttribute(EXCEPTION_ATTRIBUTE, "INVALID_TOKEN");
            throw new CustomException(TokenErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {

            request.setAttribute(EXCEPTION_ATTRIBUTE, "EXPIRED_ACCESS_TOKEN");
            throw new CustomException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        if (jwtUtil.checkBlacklist(accessToken)) {
            request.setAttribute(EXCEPTION_ATTRIBUTE, "ACCESS_TOKEN_NOT_EXIST");
            throw new CustomException(TokenErrorCode.ACCESS_TOKEN_NOT_EXIST);
        }

        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        log.info("로그인한 사용자: {}", ((CustomUserDetails)authentication.getPrincipal()).getMember().getNickname());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(HttpServletRequest request) {
        String path = request.getRequestURI();

        return excludeAllPaths.stream()
            .anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }
}
