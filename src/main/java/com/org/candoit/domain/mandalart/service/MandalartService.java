package com.org.candoit.domain.mandalart.service;

import com.org.candoit.domain.dailyaction.dto.DailyActionOverviewResponse;
import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionCustomRepository;
import com.org.candoit.domain.maingoal.dto.SimpleMainGoalInfoResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.mandalart.dto.MainGoalOverviewResponse;
import com.org.candoit.domain.mandalart.dto.SubGoalOverviewResponse;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.global.response.CustomException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MandalartService {

    private final MainGoalCustomRepository mainGoalRepository;
    private final SubGoalCustomRepository subGoalRepository;
    private final DailyActionCustomRepository dailyActionCustomRepository;

    public MainGoalOverviewResponse getMainGoalList(Member loginMember) {
        List<MainGoal> mainGoals = mainGoalRepository.findByMemberId(loginMember.getMemberId());
        List<SimpleMainGoalInfoResponse> simpleMainGoalInfo = new ArrayList<>();
        mainGoals.forEach(mainGoal -> {
            simpleMainGoalInfo.add(new SimpleMainGoalInfoResponse(mainGoal.getMainGoalId(),
                mainGoal.getMainGoalName()));
        });
        return new MainGoalOverviewResponse(simpleMainGoalInfo);
    }

    public SubGoalOverviewResponse getSubGoalList(Member loginMember, Long mainGoalId) {
        List<SubGoal> subGoals = subGoalRepository.findByMemberIdAndMainGoalId(
            loginMember.getMemberId(), mainGoalId);

        if (subGoals.isEmpty()) {
            MainGoal mainGoal = mainGoalRepository.findByMainGoalIdAndMemberId(
                    mainGoalId, loginMember.getMemberId())
                .orElseThrow(() -> new CustomException(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));
            SimpleMainGoalInfoResponse simpleMainGoalInfoResponse = new SimpleMainGoalInfoResponse(
                mainGoal.getMainGoalId(), mainGoal.getMainGoalName());
            return new SubGoalOverviewResponse(simpleMainGoalInfoResponse, List.of());
        } else {
            List<SimpleSubGoalInfoResponse> simpleSubGoalInfo = new ArrayList<>();
            subGoals.forEach(subGoal -> {
                simpleSubGoalInfo.add(
                    new SimpleSubGoalInfoResponse(subGoal.getSubGoalId(),
                        subGoal.getSubGoalName()));
            });

            SimpleMainGoalInfoResponse mainGoalInfo = new SimpleMainGoalInfoResponse(
                subGoals.get(0).getMainGoal().getMainGoalId(),
                subGoals.get(0).getMainGoal().getMainGoalName());

            return new SubGoalOverviewResponse(mainGoalInfo, simpleSubGoalInfo);
        }
    }

    public DailyActionOverviewResponse getDailyActionList(Member loginMember, Long subGoalId) {
        List<DailyAction> dailyActions = dailyActionCustomRepository.findByMemberIdAndSubGoalId(
            loginMember.getMemberId(), subGoalId);

        if (dailyActions.isEmpty()) {
            SubGoal subGoal = subGoalRepository.findByMemberIdAndSubGoalId(subGoalId,
                loginMember.getMemberId()).orElseThrow(()->new CustomException(SubGoalErrorCode.NOT_FOUND_SUB_GOAL));
            SimpleSubGoalInfoResponse simpleSubGoalInfoResponse = new SimpleSubGoalInfoResponse(
                subGoal.getSubGoalId(), subGoal.getSubGoalName());
            return new DailyActionOverviewResponse(simpleSubGoalInfoResponse, List.of());
        } else {
            List<SimpleDailyActionInfoResponse> simpleDailyActionInfo = new ArrayList<>();
            dailyActions.forEach(dailyAction -> {
                simpleDailyActionInfo.add(SimpleDailyActionInfoResponse.builder()
                    .id(dailyAction.getDailyActionId())
                    .title(dailyAction.getDailyActionTitle())
                    .content(dailyAction.getContent())
                    .targetNum(dailyAction.getTargetNum())
                    .build());
            });

            SimpleSubGoalInfoResponse subGoalInfo = new SimpleSubGoalInfoResponse(
                dailyActions.get(0).getSubGoal().getSubGoalId(),
                dailyActions.get(0).getSubGoal().getSubGoalName());
            return new DailyActionOverviewResponse(subGoalInfo, simpleDailyActionInfo);
        }
    }
}
