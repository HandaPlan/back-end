package com.org.candoit.domain.member.service;

import com.org.candoit.domain.member.dto.CheckPasswordRequest;
import com.org.candoit.domain.member.dto.MemberCheckRequest;
import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.dto.MemberUpdateRequest;
import com.org.candoit.domain.member.dto.MyPageResponse;
import com.org.candoit.domain.member.dto.NewPasswordRequest;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberRole;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.domain.member.exception.MemberErrorCode;
import com.org.candoit.domain.member.repository.MemberRepository;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.response.GlobalErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
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

    public void join(MemberJoinRequest memberJoinRequest){

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

    private void validateDuplicateOnJoin(String email, String nickname){

        if(memberRepository.existsByEmail(email)){
            throw new CustomException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }
        else if(memberRepository.existsByNickname(nickname)){
            throw new CustomException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    public Boolean check(MemberCheckRequest memberCheckRequest){

        String type = memberCheckRequest.getType();
        String content = memberCheckRequest.getContent();

        if("nickname".equals(type)){
            return memberRepository.existsByNickname(content);
        }else if("email".equals(type)){
            return memberRepository.existsByEmail(content);
        }else {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }
    }

    public MyPageResponse getMyPage(Long memberId){

        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(
            MemberErrorCode.NOT_FOUND_MEMBER));

        return MyPageResponse.builder()
            .profileImage(member.getProfilePath())
            .comment(member.getComment())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .build();
    }

    public void updatePassword(Long memberId, NewPasswordRequest newPasswordRequest){
        checkVerify(memberId, "new-password");
        Member loginMember = memberRepository.findById(memberId).orElseThrow(()->new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));
        loginMember.updatePassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
    }

    public void checkPassword(Member loginMember, CheckPasswordRequest checkPasswordRequest){
        String prefix = "check-password:"+checkPasswordRequest.getType()+":";
        if(passwordEncoder.matches(checkPasswordRequest.getPassword(), loginMember.getPassword())){
            redisTemplate.opsForValue().set(prefix + loginMember.getMemberId(),
               "checked",
                Duration.ofMinutes(5));
        }else{
            throw new CustomException(MemberErrorCode.NOT_MATCHED_PASSWORD);
        }
    }

    private void checkVerify(Long memberId, String type){
        String key = "check-password:"+type+":"+memberId;
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }else{
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }
    }

    public MyPageResponse updateInfo(Long memberId, MemberUpdateRequest memberUpdateRequest){

        Member updateMember = memberRepository.findById(memberId).orElseThrow(()->new CustomException(
            MemberErrorCode.NOT_FOUND_MEMBER));

        validateDuplicateOnUpdate(memberUpdateRequest.getEmail(), memberUpdateRequest.getNickname(), memberId);

        updateMember.updateInfo(memberUpdateRequest.getEmail(), memberUpdateRequest.getNickname(),
            memberUpdateRequest.getComment(), memberUpdateRequest.getProfile_image());

        return MyPageResponse.builder()
            .profileImage(memberUpdateRequest.getProfile_image())
            .comment(memberUpdateRequest.getComment())
            .email(memberUpdateRequest.getEmail())
            .nickname(memberUpdateRequest.getNickname())
            .build();
    }

    private void validateDuplicateOnUpdate(String updateEmail, String updateNickname, Long memberId){
        Member foundMemberByEmail = memberRepository.findByEmail(updateEmail).orElse(null);
        Member foundMemberByNickname = memberRepository.findByNickname(updateNickname).orElse(null);

        if(foundMemberByEmail != null && !foundMemberByEmail.getMemberId().equals(memberId)){
            throw new CustomException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }
        else if(foundMemberByNickname != null && !foundMemberByNickname.getMemberId().equals(memberId)){
            throw new CustomException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    public void withdraw(Long memberId, CheckPasswordRequest checkPasswordRequest) {

        Member memberToWithdraw = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));
        System.out.println("raw=" + checkPasswordRequest.getPassword());
        System.out.println("encoded=" + memberToWithdraw.getPassword());
        System.out.println("matches=" +
            passwordEncoder.matches(checkPasswordRequest.getPassword(), memberToWithdraw.getPassword()));

        if (!passwordEncoder.matches(checkPasswordRequest.getPassword(),
            memberToWithdraw.getPassword())) {
           throw new CustomException(MemberErrorCode.NOT_MATCHED_PASSWORD);
        }
        memberToWithdraw.withdraw();
    }
}
