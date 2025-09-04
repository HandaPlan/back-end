package com.org.candoit.domain.dailyprogress.service;

import com.org.candoit.domain.dailyaction.repository.DailyActionCustomRepository;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyaction.dto.ActionDates;
import com.org.candoit.domain.dailyprogress.dto.DetailProgressResponse;
import com.org.candoit.domain.dailyprogress.entity.DailyProgress;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressCustomRepository;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.global.response.CustomException;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyProgressService {

    private final DailyProgressRepository dailyProgressRepository;
    private final DailyProgressCustomRepository dailyProgressCustomRepository;
    private final DailyActionCustomRepository dailyActionCustomRepository;
    private final DailyActionRepository dailyActionRepository;
    private final SubGoalCustomRepository subGoalCustomRepository;
    private final Clock clock;

    public List<DetailProgressResponse> checkedDate(Member loginMember, Long subGoalId,
        List<ActionDates> checkDailyProgressRequest) {

        // 기준 날짜
        LocalDate now = LocalDate.now(clock);
        LocalDate startDay = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDay = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 기준 날짜 + 서브골 아이디를 통해 기존 dailyProgress를 삭제한다.
        dailyProgressCustomRepository.deleteBySubGoalIdAndDate(subGoalId, startDay, endDay);

        List<DailyProgress> progresses = checkDailyProgressRequest.stream()
                .flatMap(req ->
                    req.getDates().stream()
                        .map(date -> DailyProgress.builder()
                            .dailyAction(dailyActionRepository.getReferenceById(req.getDailyActionId()))
                            .checkedDate(date)
                            .build())
                    ).toList();

        dailyProgressRepository.saveAll(progresses);

        return getDailyProgress(loginMember, subGoalId);
    }

    public List<DetailProgressResponse> getDailyProgress(Member loginMember, Long subGoalId){
        LocalDate now = LocalDate.now(clock);
        LocalDate startDay = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDay = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        subGoalCustomRepository.findByMemberIdAndSubGoalId(loginMember.getMemberId(), subGoalId).orElseThrow(()-> new CustomException(
            SubGoalErrorCode.NOT_FOUND_SUB_GOAL));

        return dailyActionCustomRepository.getActionDates(subGoalId, startDay, endDay);
    }
}
