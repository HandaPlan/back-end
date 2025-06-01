package com.org.candoit.domain.maingoal.service;

import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.MainGoalResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.SubGoalResponse;
import com.org.candoit.domain.subgoal.entity.Color;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.global.response.CustomException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MainGoalService {

    private final MainGoalRepository mainGoalRepository;
    private final MainGoalCustomRepository mainGoalCustomRepository;
    private final SubGoalRepository subGoalRepository;

    public MainGoalResponse createMainGoal(Member member, CreateMainGoalRequest request) {

        MainGoal mainGoal = MainGoal.builder()
            .member(member)
            .mainGoalName(request.getMainGoalName())
            .mainGoalStatus(MainGoalStatus.ACTIVITY)
            .thisAchievementRate(0)
            .lastAchievementRate(0)
            .isRepresentative(Boolean.FALSE)
            .build();

        MainGoal savedMainGoal = mainGoalRepository.save(mainGoal);
        ArrayList<SubGoalResponse> subGoalResponses = new ArrayList<>();

        if (!request.getSubGoalName().isEmpty()) {
            ArrayList<SubGoal> subGoals = new ArrayList<>();

            for (int i = 0; i < request.getSubGoalName().size(); i++) {
                SubGoal subGoal = SubGoal.builder()
                    .mainGoal(mainGoal)
                    .subGoalName(request.getSubGoalName().get(i))
                    .color(Color.getColor(i))
                    .isStore(Boolean.FALSE)
                    .build();
                subGoals.add(subGoal);
            }

            List<SubGoal> subGoalList = subGoalRepository.saveAll(subGoals);
            for (SubGoal subGoal : subGoalList) {
                SubGoalResponse subGoalResponse = SubGoalResponse.builder()
                    .subGoalId(subGoal.getSubGoalId())
                    .subGoalName(subGoal.getSubGoalName())
                    .color(subGoal.getColor().getHexValue())
                    .isStore(subGoal.getIsStore())
                    .build();

                subGoalResponses.add(subGoalResponse);
            }
        }


        return MainGoalResponse.builder()
            .mainGoalId(savedMainGoal.getMainGoalId())
            .mainGoalStatus(savedMainGoal.getMainGoalStatus())
            .mainGoalName(savedMainGoal.getMainGoalName())
            .isRepresentative(savedMainGoal.getIsRepresentative())
            .subGoals(subGoalResponses)
            .build();
    }

    public Boolean deleteMainGoal(Member loginMember, Long mainGoalId) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId, loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoalRepository.delete(mainGoal);
        return Boolean.TRUE;
    }
}
