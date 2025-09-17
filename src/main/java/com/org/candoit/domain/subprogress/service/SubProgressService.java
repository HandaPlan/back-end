package com.org.candoit.domain.subprogress.service;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.DetailSubProgressResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subprogress.dto.CheckedSubProgressResponse;
import com.org.candoit.domain.subprogress.dto.DateSlotRow;
import com.org.candoit.domain.subprogress.dto.DateUnit;
import com.org.candoit.domain.subprogress.dto.Direction;
import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import com.org.candoit.domain.subprogress.dto.SubProgressOverviewResponse;
import com.org.candoit.domain.subprogress.repository.SubProgressQueryRepository;
import com.org.candoit.global.util.DateTimeUtil;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubProgressService {

    private final SubProgressQueryRepository subProgressQueryRepository;
    private final SubGoalCustomRepository subGoalCustomRepository;
    private final Clock clock;

    public SubProgressOverviewResponse getProgressWithoutSubGoal(Member loginMember,
        Long mainGoalId,
        LocalDate date, Direction direction) {

        List<SubGoal> subGoals = subGoalCustomRepository.findByMemberIdAndMainGoalId(
                loginMember.getMemberId(), mainGoalId).stream()
            .sorted(Comparator.comparing(SubGoal::getSlotNum))
            .toList();

        return getProgress(subGoals, date, direction);
    }

    public SubProgressOverviewResponse getProgress(List<SubGoal> subGoals,
        LocalDate date, Direction direction) {

        switch (direction) {
            case PREV -> date = date.minusWeeks(1);
            case NEXT -> date = date.plusWeeks(1);
            case CURRENT -> {
            }
        }

        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Long> subGoalIds = subGoals.stream()
            .map(SubGoal::getSubGoalId)
            .toList();

        List<SubProgressCalDto> subProgress = subProgressQueryRepository.aggregate(subGoalIds,
            monday, sunday);

        Map<Long, List<SubProgressCalDto>> grouping = subProgress.stream()
            .collect(Collectors.groupingBy(SubProgressCalDto::getSubGoalId));

        List<DetailSubProgressResponse> detailSubProgress = subGoals.stream()
            .map(subGoal -> {
                List<SubProgressCalDto> list = grouping.getOrDefault(subGoal.getSubGoalId(),
                    List.of());
                return new DetailSubProgressResponse(subGoal.getSubGoalName(), subGoal.getSlotNum(),
                    calculateRate(list));
            }).toList();

        int weekOfMonth = DateTimeUtil.getWeekOfMonth(date);

        return SubProgressOverviewResponse.builder()
            .startDate(monday)
            .endDate(sunday)
            .weekOfMonth(weekOfMonth)
            .subProgress(detailSubProgress)
            .build();
    }

    public int calculateRate(List<SubProgressCalDto> subProgressCalDto) {

        if (subProgressCalDto.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (SubProgressCalDto sp : subProgressCalDto) {
            int rate;
            if (sp.getProgressCount() == 0) {
                rate = 0;
            } else if (sp.getProgressCount() >= sp.getTargetNum()) {
                rate = 100;
            } else {
                rate = (int) Math.round((double) sp.getProgressCount() / sp.getTargetNum() * 100.0);
            }
            sum += rate;
        }
        return sum / subProgressCalDto.size();
    }

    public List<CheckedSubProgressResponse> getCheckedProgress(Member loginMember, Long mainGoalId,
        LocalDate date, Direction direction, DateUnit unit) {

        List<SubGoal> subGoals = subGoalCustomRepository.findByMemberIdAndMainGoalId(
                loginMember.getMemberId(), mainGoalId).stream()
            .sorted(Comparator.comparing(SubGoal::getSlotNum))
            .toList();

        List<Long> subGoalIds = subGoals.stream()
            .map(SubGoal::getSubGoalId)
            .toList();

        LocalDate start = date;
        LocalDate end = date;

        if (unit == DateUnit.MONTH) {
            switch (direction) {
                case PREV -> date = date.minusMonths(1);
                case NEXT -> date = date.plusMonths(1);
            }
            start = date.with(TemporalAdjusters.firstDayOfMonth());
            end = date.with(TemporalAdjusters.lastDayOfMonth());
        } else if (unit == DateUnit.WEEK) {
            switch (direction) {
                case PREV -> date = date.minusWeeks(1);
                case NEXT -> date = date.plusWeeks(1);
            }
            start = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            end = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        }

        List<DateSlotRow> rows = subProgressQueryRepository.getCheckedSubProgressByDate(subGoalIds,
            start, end);

        Map<LocalDate, List<Integer>> grouping = rows.stream()
            .collect(Collectors.groupingBy(
                DateSlotRow::checkedDate,
                LinkedHashMap::new,
                Collectors.mapping(DateSlotRow::slotNum, Collectors.toList())

            ));

        grouping.replaceAll((d, list) ->
            list.stream().distinct().sorted().toList());

        return grouping.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> new CheckedSubProgressResponse(e.getKey(), e.getValue()))
            .toList();
    }
}
