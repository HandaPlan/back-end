package com.org.candoit.domain.subgoal.repository;

import static com.org.candoit.domain.maingoal.entity.QMainGoal.mainGoal;
import static com.org.candoit.domain.subgoal.entity.QSubGoal.subGoal;

import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubGoalCustomRepositoryImpl implements SubGoalCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SubGoal> findByMemberId(Long memberId) {
        return jpaQueryFactory.select(subGoal)
            .from(subGoal)
            .innerJoin(subGoal.mainGoal, mainGoal)
            .on(mainGoal.mainGoalId.eq(subGoal.mainGoal.mainGoalId))
            .where(mainGoal.member.memberId.eq(memberId))
            .fetch();
    }

    @Override
    public List<SubGoal> findByMemberIdAndMainGoalId(Long memberId, Long mainGoalId) {
        return jpaQueryFactory.select(subGoal)
            .from(subGoal)
            .innerJoin(subGoal.mainGoal, mainGoal)
            .on(mainGoal.mainGoalId.eq(subGoal.mainGoal.mainGoalId))
            .where(mainGoal.member.memberId.eq(memberId).and(
                subGoal.mainGoal.mainGoalId.eq(mainGoalId)
            ))
            .fetch();
    }
}
