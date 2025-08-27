package com.org.candoit.domain.subgoal.service;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.SimpleInfoWithAttainmentResponse;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.dto.UpdateSubGoalRequest;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
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
    private final SubGoalCustomRepository subGoalCustomRepository;
    private final MainGoalCustomRepository mainGoalCustomRepository;
    private final DailyActionRepository dailyActionRepository;

    public SimpleSubGoalInfoResponse createSubGoal(Member loginMember, Long mainGoalId,
        CreateSubGoalRequest createSubGoalRequest) {

        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
            loginMember.getMemberId()).orElseThrow(() -> new CustomException(
            MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        int alreadySavedSubGoalSize = subGoalCustomRepository.findByMainGoalId(mainGoal.getMainGoalId()).size();

        SubGoal subGoal = SubGoal.builder()
            .mainGoal(mainGoal)
            .subGoalName(createSubGoalRequest.getName())
            .isStore(Boolean.FALSE)
            .slotNum(alreadySavedSubGoalSize + 1)
            .build();
        SubGoal savedSubGoal = subGoalRepository.save(subGoal);

        if (!createSubGoalRequest.getDailyActions().isEmpty()) {
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

            dailyActionRepository.saveAll(dailyActions);
        }

        return new SimpleSubGoalInfoResponse(savedSubGoal.getSubGoalId(),
            savedSubGoal.getSubGoalName());
    }

    public SimpleInfoWithAttainmentResponse updateSubGoal(Member loginMember, Long subGoalId,
        UpdateSubGoalRequest updateSubGoalRequest) {

        SubGoal updateSubgoal = subGoalCustomRepository.findByMemberIdAndSubGoalId(
                loginMember.getMemberId(), subGoalId)
            .orElseThrow(() -> new CustomException(SubGoalErrorCode.NOT_FOUND_SUB_GOAL));

        updateSubgoal.changeSubGoalProperty(updateSubGoalRequest.getName(),
            updateSubGoalRequest.getAttainment());
        return new SimpleInfoWithAttainmentResponse(updateSubgoal.getSubGoalId(),
            updateSubgoal.getSubGoalName(), updateSubGoalRequest.getAttainment());
    }
}
