package com.org.candoit.domain.maingoal.service;

import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.MainGoalResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.entity.Color;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MainGoalService {

    private final MainGoalRepository mainGoalRepository;
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

        if(!request.getSubGoalName().isEmpty()){
            ArrayList<SubGoal> subGoals = new ArrayList<>();

            for (int i = 0; i < request.getSubGoalName().size(); i++) {
                SubGoal subGoal = SubGoal.builder()
                    .mainGoal(mainGoal)
                    .subGoalTitle(request.getSubGoalName().get(i))
                    .color(Color.getColor(i))
                    .isStore(Boolean.FALSE)
                    .build();
                subGoals.add(subGoal);
            }

            subGoalRepository.saveAll(subGoals);
        }

        return MainGoalResponse.builder()
            .mainGoalId(savedMainGoal.getMainGoalId())
            .mainGoalStatus(savedMainGoal.getMainGoalStatus())
            .mainGoalName(savedMainGoal.getMainGoalName())
            .isRepresentative(savedMainGoal.getIsRepresentative())
            .build();
    }
}
