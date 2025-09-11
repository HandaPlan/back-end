package com.org.candoit.domain.subprogress.repository;

import static com.org.candoit.domain.dailyaction.entity.QDailyAction.dailyAction;
import static com.org.candoit.domain.dailyprogress.entity.QDailyProgress.dailyProgress;

import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubProgressQueryRepositoryImpl implements SubProgressQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SubProgressCalDto> aggregate(List<Long> subGoalIds, LocalDate start,
        LocalDate end) {

        if (subGoalIds.isEmpty()) {
            return List.of();
        }

        List<SubProgressCalDto> subProgressCalDto = jpaQueryFactory.select(
                Projections.constructor(
                    SubProgressCalDto.class,
                    dailyAction.subGoal.subGoalId,
                    dailyAction.dailyActionId,
                    dailyAction.targetNum,
                    dailyProgress.checkedDate.countDistinct()
                )
            )
            .from(dailyAction)
            .leftJoin(dailyProgress)
            .on(dailyProgress.dailyAction.eq(dailyAction),
                dailyProgress.checkedDate.between(start, end))
            .where(
                dailyAction.subGoal.subGoalId.in(subGoalIds)
            )
            .groupBy(dailyAction.subGoal.subGoalId, dailyAction.dailyActionId,
                dailyAction.targetNum)
            .fetch();

        return subProgressCalDto;
    }
}
