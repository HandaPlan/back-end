package com.org.candoit.domain.mandalart.service;

import com.org.candoit.domain.maingoal.dto.SimpleMainGoalInfoResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.mandalart.dto.MainGoalOverviewResponse;
import com.org.candoit.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MandalartService {

    private final MainGoalCustomRepository mainGoalRepository;

    public MainGoalOverviewResponse getMainGoalList(Member loginMember) {
        List<MainGoal> mainGoals = mainGoalRepository.findByMemberId(loginMember.getMemberId());
        List<SimpleMainGoalInfoResponse> simpleMainGoalInfo = new ArrayList<>();
        mainGoals.forEach(mainGoal -> {
            simpleMainGoalInfo.add(new SimpleMainGoalInfoResponse(mainGoal.getMainGoalId(),
                mainGoal.getMainGoalName()));
        });
        return new MainGoalOverviewResponse(simpleMainGoalInfo);
    }
}
