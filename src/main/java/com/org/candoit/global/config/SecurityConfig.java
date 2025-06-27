package com.org.candoit.global.config;

import com.org.candoit.global.security.CustomAuthenticationProvider;
import com.org.candoit.global.security.basic.CustomUserDetailsService;
import com.org.candoit.global.security.exception.CustomAuthenticationEntryPoint;
import com.org.candoit.global.security.filter.JwtAuthorizationFilter;
import com.org.candoit.global.security.jwt.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CorsConfig corsConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder());
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUtil jwtUtil) {
        return new JwtAuthorizationFilter(jwtUtil);
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(customAuthenticationProvider()));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                // 문서화 api
                .requestMatchers("/",
                    "/swagger-ui/**",
                    "/v3/api-docs/**").permitAll()
                // 로그인, 회원가입, 토큰 재발행
                .requestMatchers("/api/auth/login", "/api/members/join", "/api/auth/reissue"
                    ).permitAll()
                .requestMatchers("/api/members/check", "/api/auth/logout", "/api/main-goals").hasRole("USER")
                .anyRequest().authenticated()
            );

        // 기본 로그인 관련 설정
        http
            .formLogin(auth -> auth.disable())
            .httpBasic(auth -> auth.disable())
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        http
            .addFilterAfter(jwtAuthorizationFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(
            exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint));


        return http.build();
    }
}
