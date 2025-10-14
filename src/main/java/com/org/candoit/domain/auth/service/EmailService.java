package com.org.candoit.domain.auth.service;

import com.org.candoit.domain.auth.dto.EmailRequest;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.domain.member.exception.MemberErrorCode;
import com.org.candoit.domain.member.repository.MemberRepository;
import com.org.candoit.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MemberRepository memberRepository;
    private final EmailSenderService emailSenderService;

    public void emailVerification(EmailRequest emailRequest) {
        Member member = memberRepository.findByEmailAndMemberStatus(emailRequest.getEmail(),
                MemberStatus.ACTIVITY)
            .orElseThrow(() -> new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));
        emailSenderService.sendEmail(member.getEmail());
    }

}
