package com.org.candoit.domain.member.dto;

import com.org.candoit.domain.member.entity.MemberRole;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.global.security.basic.CustomUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Setter
@Getter
public class BasicMemberInfoResponse {

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    private Long memberId;

    private String email;

    private String nickname;

    private String comment;

    private String profilePath;

    private MemberStatus memberStatus;

    private MemberRole memberRole;

    public BasicMemberInfoResponse(CustomUserDetails customUserDetails) {

        this.memberId = customUserDetails.getMember().getMemberId();
        this.email = customUserDetails.getMember().getEmail();
        this.nickname = customUserDetails.getMember().getNickname();
        this.comment = customUserDetails.getMember().getComment();
        this.profilePath = "https://" +cloudFrontDomain+ "/" + customUserDetails.getMember().getProfilePath();
        this.memberStatus = customUserDetails.getMember().getMemberStatus();
        this.memberRole = customUserDetails.getMember().getMemberRole();
    }
}
