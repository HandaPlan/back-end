package com.org.candoit.domain.member.entity;

import com.org.candoit.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;

    private String password;

    private String nickname;

    private String comment;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public void updateInfo(String email, String nickname, String comment, String profilePath){

        this.email = email;
        this.nickname = nickname;
        this.comment = comment;
        this.profilePath = profilePath;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void withdraw(){

        this.memberStatus = MemberStatus.WITHDRAW;
    }
}
