package com.org.candoit.domain.auth.service;

import com.org.candoit.domain.auth.dto.VerifyCodeRequest;
import com.org.candoit.domain.auth.exception.AuthErrorCode;
import com.org.candoit.global.response.CustomException;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailVerificationService {

    private final RedisTemplate<String, String> redisTemplate;
    static final String PREFIX = "auth:email:";

    public void verifyCode(VerifyCodeRequest verifyCodeRequest) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String email = verifyCodeRequest.getEmail();
        String code = verifyCodeRequest.getCode();

        String correctCode = valueOperations.get(PREFIX + email);

        if (correctCode == null) {
            throw new CustomException(AuthErrorCode.INVALID_AUTH_CODE);
        } else if (!correctCode.equals(code)) {
            throw new CustomException(AuthErrorCode.NOT_MATCH_AUTH_CODE);
        } else {
            redisTemplate.delete(PREFIX + email);

            // 인증된 사용자 저장
            String newPrefix = PREFIX + "new-password:";
            valueOperations.set(newPrefix + email, "authenticated", 10,
                TimeUnit.MINUTES);
        }
    }
}
