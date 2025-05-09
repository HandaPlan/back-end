package com.org.candoit.global.security;

import com.org.candoit.domain.member.exception.MemberErrorCode;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.security.basic.CustomUserDetails;
import com.org.candoit.global.security.basic.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails customUserDetails = null;

        try {
            customUserDetails = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new CustomException(MemberErrorCode.NOT_FOUND_MEMBER);
        } catch (DisabledException e) {
            throw new CustomException(MemberErrorCode.WITHDRAWN_MEMBER);
        }

        if(password != null && !bCryptPasswordEncoder.matches(password, customUserDetails.getPassword())) {
            throw new CustomException(MemberErrorCode.NOT_FOUND_MEMBER);
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, null,
            customUserDetails.getAuthorities());
    }
}
