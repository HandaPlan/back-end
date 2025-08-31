package com.org.candoit.domain.dailyaction.repository;

import static com.org.candoit.domain.dailyaction.entity.QDailyAction.dailyAction;
import static com.org.candoit.domain.maingoal.entity.QMainGoal.mainGoal;
import static com.org.candoit.domain.subgoal.entity.QSubGoal.subGoal;

import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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
    public List<DailyActionInfoWithAttainmentResponse> getSimpleDailyActionInfo(Long subGoalId) {
        return jpaQueryFactory.select(Projections.constructor(DailyActionInfoWithAttainmentResponse.class,
                dailyAction.dailyActionId,
                dailyAction.dailyActionTitle,
                dailyAction.content,
                dailyAction.targetNum,
                dailyAction.isStore
            )).from(dailyAction)
            .innerJoin(dailyAction.subGoal, subGoal)
            .on(dailyAction.subGoal.subGoalId.eq(subGoalId))
            .fetch();
    }

    @Override
    public Optional<DailyAction> findByMemberIdAndDailyActionId(Long memberId, Long dailyActionId) {
        return Optional.ofNullable(jpaQueryFactory.select(dailyAction)
            .from(dailyAction)
            .join(dailyAction.subGoal, subGoal)
            .join(subGoal.mainGoal, mainGoal)
            .where(
                dailyAction.dailyActionId.eq(dailyActionId),
                mainGoal.member.memberId.eq(memberId)
            ).fetchOne());
    }
}
