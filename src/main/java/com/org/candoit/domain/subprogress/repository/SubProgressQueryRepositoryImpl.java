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
    public List<SubProgressCalDto> aggregate(Long subGoalId, LocalDate start, LocalDate end) {
        List<SubProgressCalDto> subProgressCalDto = jpaQueryFactory.select(
                Projections.constructor(
                    SubProgressCalDto.class,
                    dailyAction.dailyActionId,
                    dailyAction.targetNum,
                    dailyProgress.dailyProgressId.count().intValue()
                )
            )
            .from(dailyAction)
            .leftJoin(dailyProgress)
            .on(dailyProgress.dailyAction.eq(dailyAction)
                .and(dailyProgress.checkedDate.between(start, end)))
            .where(dailyAction.subGoal.subGoalId.eq(subGoalId))
            .groupBy(dailyAction.dailyActionId)
            .fetch();

        return subProgressCalDto;
    }
}
