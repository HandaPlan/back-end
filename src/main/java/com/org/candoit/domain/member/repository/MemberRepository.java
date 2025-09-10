package com.org.candoit.domain.member.repository;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndMemberStatus(String email, MemberStatus memberStatus);
    Optional<Member> findByNicknameAndMemberStatus(String nickname, MemberStatus memberStatus);
    boolean existsByEmailAndMemberStatus(String email, MemberStatus memberStatus);
    boolean existsByNicknameAndMemberStatus(String nickname, MemberStatus memberStatus);
}
