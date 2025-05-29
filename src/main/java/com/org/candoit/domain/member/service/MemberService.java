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

    public Boolean check(MemberCheckRequest memberCheckRequest){

        String type = memberCheckRequest.getType();
        String content = memberCheckRequest.getContent();

        if(type.equals("nickname")){
            return memberRepository.findByNickname(content).isPresent();
        }else if(type.equals("email")){
            return memberRepository.findByEmail(content).isPresent();
        }else {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST);
        }
    }

    public MyPageResponse getMyPage(Long memberId){

        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(
            MemberErrorCode.NOT_FOUND_MEMBER));

        return MyPageResponse.builder()
            .profile_image(member.getProfilePath())
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

        updateMember.updateInfo(memberUpdateRequest.getEmail(), memberUpdateRequest.getNickname(),
            memberUpdateRequest.getComment(), memberUpdateRequest.getProfile_image());

        return MyPageResponse.builder()
            .profile_image(memberUpdateRequest.getProfile_image())
            .comment(memberUpdateRequest.getComment())
            .email(memberUpdateRequest.getEmail())
            .nickname(memberUpdateRequest.getNickname())
            .build();
    }

    public void withdraw(Long memberId, CheckPasswordRequest checkPasswordRequest){

        Member memberToWithdraw = memberRepository.findById(memberId).orElseThrow(()->new CustomException(MemberErrorCode.NOT_FOUND_MEMBER));

        checkVerify(memberId, "withdraw");

        memberToWithdraw.withdraw();
    }
}
