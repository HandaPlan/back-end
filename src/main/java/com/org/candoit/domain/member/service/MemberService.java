package com.org.candoit.domain.member.service;

import com.org.candoit.domain.member.dto.MemberJoinRequest;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.repository.MemberRepository;
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
}
