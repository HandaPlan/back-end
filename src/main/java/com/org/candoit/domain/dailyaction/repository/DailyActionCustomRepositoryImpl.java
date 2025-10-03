package com.org.candoit.domain.dailyaction.repository;

import static com.org.candoit.domain.dailyaction.entity.QDailyAction.dailyAction;
import static com.org.candoit.domain.dailyprogress.entity.QDailyProgress.dailyProgress;
import static com.org.candoit.domain.maingoal.entity.QMainGoal.mainGoal;
import static com.org.candoit.domain.subgoal.entity.QSubGoal.subGoal;

import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyprogress.dto.DetailProgressResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<DetailProgressResponse> getActionDates(Long subGoalId, LocalDate start,
        LocalDate end) {
        List<DailyProgressRow> result = jpaQueryFactory
            .select(Projections.constructor(
                DailyProgressRow.class,
                dailyAction.dailyActionId,
                dailyAction.dailyActionTitle,
                dailyAction.content,
                dailyProgress.checkedDate
            ))
            .from(dailyAction)
            .leftJoin(dailyProgress)
            .on(dailyProgress.dailyAction.eq(dailyAction),
                dailyProgress.checkedDate.between(start, end))
            .where(dailyAction.subGoal.subGoalId.eq(subGoalId))
            .orderBy(dailyAction.dailyActionId.asc(), dailyProgress.checkedDate.asc())
            .fetch();

        return groupByDailyAction(result);
    }

    private List<DetailProgressResponse> groupByDailyAction(
        List<DailyProgressRow> result) {
        Map<Long, DetailProgressResponse> map = new LinkedHashMap<>();

        for (DailyProgressRow row : result) {
            map.compute(row.dailyActionId(), (id, existing) -> {
                if (existing == null) {
                    List<LocalDate> dates = new ArrayList<>();
                    if (row.checkedDate() != null)
                        dates.add(row.checkedDate());

                    return DetailProgressResponse.builder()
                        .dailyActionId(row.dailyActionId())
                        .title(row.title())
                        .content(row.content())
                        .checkedDate(dates)
                        .build();
                }
                else{
                    if(row.checkedDate() != null) existing.getCheckedDate().add(row.checkedDate());
                    return existing;
                }
            });
        }
        return map.values().stream().toList();
    }

    public record DailyProgressRow(Long dailyActionId, String title, String content,
                                           LocalDate checkedDate) {

    }
}
