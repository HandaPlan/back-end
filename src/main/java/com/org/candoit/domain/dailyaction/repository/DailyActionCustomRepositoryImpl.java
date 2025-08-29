package com.org.candoit.domain.dailyaction.repository;

import static com.org.candoit.domain.dailyaction.entity.QDailyAction.dailyAction;
import static com.org.candoit.domain.maingoal.entity.QMainGoal.mainGoal;
import static com.org.candoit.domain.subgoal.entity.QSubGoal.subGoal;

import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailyActionCustomRepositoryImpl implements DailyActionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DailyAction> findByMemberIdAndSubGoalId(Long memberId, Long subGoalId) {
        return jpaQueryFactory.select(dailyAction)
            .from(dailyAction)
            .innerJoin(dailyAction.subGoal, subGoal)
            .innerJoin(subGoal.mainGoal, mainGoal)
            .on(mainGoal.mainGoalId.eq(subGoal.mainGoal.mainGoalId))
            .where(subGoal.subGoalId.eq(subGoalId).and(mainGoal.member.memberId.eq(memberId)
            ))
            .fetch();
    }

    @Override
    public List<SimpleDailyActionInfoResponse> getSimpleDailyActionInfo(Long subGoalId) {
        return jpaQueryFactory.select(Projections.constructor(SimpleDailyActionInfoResponse.class,
            dailyAction.dailyActionId,
            dailyAction.dailyActionTitle,
            dailyAction.content,
            dailyAction.targetNum
            )).from(dailyAction)
            .innerJoin(dailyAction.subGoal, subGoal)
            .on(dailyAction.subGoal.subGoalId.eq(subGoalId))
            .fetch();
    }
}
