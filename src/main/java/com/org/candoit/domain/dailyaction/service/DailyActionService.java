package com.org.candoit.domain.dailyaction.service;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DailyActionService {

    private final DailyActionRepository dailyActionRepository;
    private final SubGoalCustomRepository subGoalCustomRepository;

    public DailyActionInfoWithAttainmentResponse createDailyAction(Member loginMember,
        Long subGoalId, CreateDailyActionRequest dailyActionRequest) {

        SubGoal subGoal = subGoalCustomRepository.findByMemberIdAndSubGoalId(
            loginMember.getMemberId(), subGoalId).orElseThrow(() -> new CustomException(
            SubGoalErrorCode.NOT_FOUND_SUB_GOAL));

        DailyAction dailyAction = DailyAction.builder().
            dailyActionTitle(dailyActionRequest.getTitle())
            .content(dailyActionRequest.getContent())
            .subGoal(subGoal)
            .targetNum(dailyActionRequest.getTargetNum()).build();


        DailyAction savedDailyAction = dailyActionRepository.save(dailyAction);
        return DailyActionInfoWithAttainmentResponse.builder()
            .id(savedDailyAction.getDailyActionId())
            .title(savedDailyAction.getDailyActionTitle())
            .content(savedDailyAction.getContent())
            .targetNum(savedDailyAction.getTargetNum())
            .attainment(savedDailyAction.getIsStore())
            .build();
    }
}
