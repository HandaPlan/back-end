package com.org.candoit.domain.subgoal.service;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyprogress.entity.DailyProgress;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.entity.SubProgress;
import com.org.candoit.domain.subprogress.repository.SubProgressRepository;
import com.org.candoit.global.response.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SubGoalService {

    private final SubGoalRepository subGoalRepository;
    private final MainGoalCustomRepository mainGoalCustomRepository;
    private final SubProgressRepository subProgressRepository;
    private final DailyActionRepository dailyActionRepository;
    private final DailyProgressRepository dailyProgressRepository;

    public SimpleSubGoalInfoResponse createSubGoal(Member loginMember, Long mainGoalId,
        CreateSubGoalRequest createSubGoalRequest) {

        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
            loginMember.getMemberId()).orElseThrow(() -> new CustomException(
            MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        SubGoal subGoal = SubGoal.builder()
            .mainGoal(mainGoal)
            .subGoalName(createSubGoalRequest.getName())
            .isStore(Boolean.FALSE)
            .slotNum(mainGoal.getSubGoals().size() + 1)
            .build();
        SubGoal savedSubGoal = subGoalRepository.save(subGoal);

        SubProgress subProgress = SubProgress.builder()
            .subGoal(savedSubGoal)
            .checkedCount(0)
            .targetCount(0)
            .build();
        subProgressRepository.save(subProgress);

        if(!createSubGoalRequest.getDailyActions().isEmpty()){
            List<DailyAction> dailyActions = createSubGoalRequest.getDailyActions().stream()
                .map(dailyAction ->
                    DailyAction.builder()
                        .dailyActionTitle(dailyAction.getTitle())
                        .content(dailyAction.getContent())
                        .subGoal(savedSubGoal)
                        .targetNum(dailyAction.getTargetNum())
                        .isStore(Boolean.FALSE)
                        .build())
                .collect(Collectors.toList());

            List<DailyAction> savedDailyActions = dailyActionRepository.saveAll(dailyActions);

            List<DailyProgress> dailyProgresses = savedDailyActions.stream()
                .map(dailyAction -> DailyProgress.builder().dailyAction(dailyAction).build())
                .collect(Collectors.toList());

            dailyProgressRepository.saveAll(dailyProgresses);
        }

        return new SimpleSubGoalInfoResponse(savedSubGoal.getSubGoalId(),
            savedSubGoal.getSubGoalName());
    }
}
