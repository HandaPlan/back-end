package com.org.candoit.domain.member.service;

import com.org.candoit.domain.auth.exception.AuthErrorCode;
import com.org.candoit.domain.member.dto.CheckPasswordRequest;
import com.org.candoit.domain.member.dto.MemberCheckRequest;
import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.dto.MemberUpdateRequest;
import com.org.candoit.domain.member.dto.MyPageResponse;
import com.org.candoit.domain.member.dto.NewPasswordRequest;
import com.org.candoit.domain.member.dto.ResetPasswordRequest;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberRole;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.domain.member.exception.MemberErrorCode;
import com.org.candoit.domain.member.repository.MemberRepository;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.response.GlobalErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    public void join(MemberJoinRequest memberJoinRequest) {

        validateDuplicateOnJoin(memberJoinRequest.getEmail(), memberJoinRequest.getNickname());

        Member saveRequestMember = Member.builder()
            .email(memberJoinRequest.getEmail())
            .comment("안녕하세요.")
            .password(passwordEncoder.encode(memberJoinRequest.getPassword()))
            .nickname(memberJoinRequest.getNickname())
            .memberStatus(MemberStatus.ACTIVITY)
            .memberRole(MemberRole.ROLE_USER)
            .build();

        memberRepository.save(saveRequestMember);
    }

    private void validateDuplicateOnJoin(String email, String nickname) {

        if (memberRepository.existsByEmailAndMemberStatus(email, MemberStatus.ACTIVITY)) {
            throw new CustomException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        } else if (memberRepository.existsByNicknameAndMemberStatus(nickname,
            MemberStatus.ACTIVITY)) {
            throw new CustomException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    public Boolean check(MemberCheckRequest memberCheckRequest) {

        String type = memberCheckRequest.getType();
        String content = memberCheckRequest.getContent();

        if ("nickname".equals(type)) {
            return memberRepository.existsByNicknameAndMemberStatus(content, MemberStatus.ACTIVITY);
        } else if ("email".equals(type)) {
            return memberRepository.existsByEmailAndMemberStatus(content, MemberStatus.ACTIVITY);
        } else {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }
    }

    public MyPageResponse getMyPage(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(
            MemberErrorCode.NOT_FOUND_MEMBER));

        return MyPageResponse.builder()
            .profileImage(member.getProfilePath())
            .comment(member.getComment())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .build();
    }

    public void updatePassword(Long memberId, NewPasswordRequest newPasswordRequest) {
        checkVerify(memberId, "new-password");
        Member loginMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));
        loginMember.updatePassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
    }

    public void checkPassword(Member loginMember, CheckPasswordRequest checkPasswordRequest) {
        String prefix = "check-password:" + checkPasswordRequest.getType() + ":";
        if (passwordEncoder.matches(checkPasswordRequest.getPassword(),
            loginMember.getPassword())) {
            redisTemplate.opsForValue().set(prefix + loginMember.getMemberId(),
                "checked",
                Duration.ofMinutes(5));
        } else {
            throw new CustomException(MemberErrorCode.NOT_MATCHED_PASSWORD);
        }
    }

    private void checkVerify(Long memberId, String type) {
        String key = "check-password:" + type + ":" + memberId;
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        } else {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }
    }

    public MyPageResponse updateInfo(Long memberId, MemberUpdateRequest memberUpdateRequest) {

        Member updateMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(
                MemberErrorCode.NOT_FOUND_MEMBER));

        if (memberUpdateRequest.getNickname() != null) {
            validateDuplicateOnUpdate(memberUpdateRequest.getNickname(),
                updateMember.getNickname());
        }

        updateMember.updateInfo(memberUpdateRequest.getNickname(),
            memberUpdateRequest.getComment(), memberUpdateRequest.getProfileImage());

        return MyPageResponse.builder()
            .profileImage("https://" +cloudFrontDomain+ "/" + updateMember.getProfilePath())
            .comment(updateMember.getComment())
            .email(updateMember.getEmail())
            .nickname(updateMember.getNickname())
            .build();
    }

    private void validateDuplicateOnUpdate(String updateNickname, String originNickName) {

        Member foundMemberByNickname = memberRepository.findByNicknameAndMemberStatus(
            updateNickname, MemberStatus.ACTIVITY).orElse(null);
        if (foundMemberByNickname != null && !originNickName.equals(updateNickname)) {
            throw new CustomException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    public void withdraw(Long memberId, CheckPasswordRequest checkPasswordRequest) {

        Member memberToWithdraw = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(checkPasswordRequest.getPassword(),
            memberToWithdraw.getPassword())) {
            throw new CustomException(MemberErrorCode.NOT_MATCHED_PASSWORD);
        }
        memberToWithdraw.withdraw();
    }

    public Boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {

        Member member = validateEmailAndEmailAuthentication(resetPasswordRequest.getEmail());
        member.updatePassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        deleteEmailAuthenticationData(resetPasswordRequest.getEmail());

        return Boolean.TRUE;
    }

    private Member validateEmailAndEmailAuthentication(String email) {

        Member member = memberRepository.findByEmailAndMemberStatus(email, MemberStatus.ACTIVITY)
            .orElseThrow(() -> new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));

        String prefix = "auth:email:new-password:";

        Boolean hasKeyInMemory = redisTemplate.hasKey(prefix + email);

        if (Boolean.FALSE.equals(hasKeyInMemory)) {
            throw new CustomException(AuthErrorCode.EMAIL_VERIFICATION_REQUIRED);
        } else if (hasKeyInMemory == null) {
            throw new CustomException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
        }

        return member;
    }

    private void deleteEmailAuthenticationData(String email) {

        redisTemplate.delete("auth:email:new-password:" + email);
    }
}
