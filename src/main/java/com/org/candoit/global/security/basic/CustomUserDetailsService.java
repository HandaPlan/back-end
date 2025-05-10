package com.org.candoit.global.security.basic;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.domain.member.exception.MemberErrorCode;
import com.org.candoit.domain.member.repository.MemberRepository;
import com.org.candoit.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = memberRepository.findByEmail(username).orElseThrow(()->new CustomException(
            MemberErrorCode.NOT_FOUND_MEMBER));

        if(findMember.getMemberStatus() == MemberStatus.WITHDRAW){
            throw new DisabledException(MemberErrorCode.WITHDRAWN_MEMBER.getMessage());
        }

        return new CustomUserDetails(findMember);
    }
}
