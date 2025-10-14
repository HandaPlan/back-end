package com.org.candoit.domain.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    static final String PREFIX = "auth:email:";

    @Async("mailExecutor")
    public void sendEmail(String email) {
        log.info("[Thread: {}] sending mail to {}", Thread.currentThread().getName(), email);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String code = createCode();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[한다라트] 인증코드가 발송되었습니다.");
            mimeMessageHelper.setText(setContent(code), true);
            javaMailSender.send(mimeMessage);
            codeSave(email, code);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패 : {}", email);
            throw new RuntimeException(e);
        }
    }

    private String createCode() {

        SecureRandom secureRandom = new SecureRandom();
        int authenticationCode = secureRandom.nextInt((int) Math.pow(10, 6));
        return String.format("%06d", authenticationCode);
    }

    private String setContent(String code) {

        Context context = new Context();
        context.setVariable("code", code);

        String content = templateEngine.process("change-password", context);

        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Generated content is empty");
        }

        return content;
    }

    private void codeSave(String email, String code) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(PREFIX + email, code, 3, TimeUnit.MINUTES);
    }
}
