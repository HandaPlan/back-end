package com.org.candoit.domain.dailyprogress.repository;

import static com.org.candoit.domain.dailyaction.entity.QDailyAction.dailyAction;
import static com.org.candoit.domain.dailyprogress.entity.QDailyProgress.dailyProgress;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailyProgressCustomRepositoryImpl implements DailyProgressCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LocalDate> distinctCheckedDate(Long subGoalId, LocalDate start, LocalDate end) {
        return jpaQueryFactory.select(dailyProgress.checkedDate).distinct()
            .from(dailyProgress)
            .where(
                dailyProgress.checkedDate.between(start, end),
                JPAExpressions.selectOne()
                    .from(dailyAction)
                    .where(dailyAction.dailyActionId.eq(dailyProgress.dailyAction.dailyActionId),
                        dailyAction.subGoal.subGoalId.eq(subGoalId))
                    .exists()
            ).fetch();
    }

    @Override
    public void deleteByDailyActionIdAndCheckedDate(Long dailyActionId, LocalDate checkedDate) {
        jpaQueryFactory.delete(dailyProgress)
            .where(dailyProgress.dailyAction.dailyActionId.eq(dailyActionId).and(
                dailyProgress.checkedDate.eq(checkedDate)
            )).execute();

    }
}
