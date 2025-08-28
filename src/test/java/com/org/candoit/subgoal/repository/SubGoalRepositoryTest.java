package com.org.candoit.subgoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberRole;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SubGoalRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SubGoalRepositoryTest.class);
    @Autowired SubGoalRepository subGoalRepository;
    @Autowired DailyActionRepository dailyActionRepository;
    @Autowired TestEntityManager tm;

    @Test
    void givenSubGoalAndDailyAction_whenDeleteSubGoal_thenDeleteBoth() {
        // given
        Member member = tm.persist(Member.builder()
            .email("test@email.com")
            .nickname("닉네임")
            .password("password")
            .memberRole(MemberRole.ROLE_USER)
            .memberStatus(MemberStatus.ACTIVITY)
            .build());

        MainGoal mainGoal = tm.persist(MainGoal.builder()
            .member(member)
            .mainGoalName("메인골")
            .mainGoalStatus(MainGoalStatus.ACTIVITY)
            .isRepresentative(false)
            .build());

        SubGoal subGoal = tm.persist(SubGoal.builder()
            .mainGoal(mainGoal)
            .subGoalName("서브골")
            .isStore(false)
            .slotNum(1)
            .build());

        for (int i = 0; i < 5; i++) {
            tm.persist(DailyAction.builder()
                .dailyActionTitle("데일리 액션 " + i)
                .subGoal(subGoal)
                .targetNum(4)
                .isStore(false)
                .content("내용")
                .build());
        }

        tm.flush(); tm.clear();
        Long subGoalId = subGoal.getSubGoalId();

        // when
        subGoalRepository.deleteById(subGoalId);
        tm.flush(); tm.clear();

        // then
        assertTrue(subGoalRepository.findById(subGoalId).isEmpty());

        Long cnt = ((Number) tm.getEntityManager()
            .createNativeQuery("SELECT COUNT(*) FROM daily_action WHERE sub_goal_id = :id")
            .setParameter("id", subGoalId)
            .getSingleResult()).longValue();
        assertEquals(0L, cnt);
    }
}