package com.org.candoit.domain.dailyaction.service;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.dto.UpdateDailyActionRequest;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.exception.DailyActionErrorCode;
import com.org.candoit.domain.dailyaction.repository.DailyActionCustomRepository;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class DailyActionService {

    private final DailyActionRepository dailyActionRepository;
    private final DailyActionCustomRepository dailyActionCustomRepository;
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

    public DailyActionInfoWithAttainmentResponse updateDailyAction(Member loginMember, Long dailyActionId, UpdateDailyActionRequest request) {

        DailyAction dailyAction = dailyActionCustomRepository.findByMemberIdAndDailyActionId(loginMember.getMemberId(), dailyActionId).orElseThrow(()->new CustomException(
            DailyActionErrorCode.NOT_FOUND_DAILY_ACTION));
        dailyAction.updateDailyAction(request.getTitle(), request.getContent(), request.getTargetNum(), request.getAttainment());
        return DailyActionInfoWithAttainmentResponse.builder()
            .id(dailyAction.getDailyActionId())
            .title(dailyAction.getDailyActionTitle())
            .content(dailyAction.getContent())
            .targetNum(dailyAction.getTargetNum())
            .attainment(dailyAction.getIsStore())
            .build();
    }
}
