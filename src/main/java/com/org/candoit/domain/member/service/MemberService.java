package com.org.candoit.domain.member.service;

import com.org.candoit.domain.member.dto.MemberCheckRequest;
import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.repository.MemberRepository;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(MemberJoinRequest memberJoinRequest){

        Member saveRequestMember = Member.builder()
            .email(memberJoinRequest.getEmail())
            .password(memberJoinRequest.getPassword())
            .nickname(memberJoinRequest.getNickname())
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
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}
