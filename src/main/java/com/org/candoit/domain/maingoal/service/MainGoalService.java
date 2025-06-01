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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

        List<SubGoal> savedSubGoals = new ArrayList<>();

        if (!request.getSubGoalName().isEmpty()) {

            List<SubGoal> subGoals = IntStream.range(0, request.getSubGoalName().size())
                .mapToObj(i -> SubGoal.builder()
                    .mainGoal(savedMainGoal)
                    .subGoalName(request.getSubGoalName().get(i))
                    .color(Color.getColor(i))
                    .isStore(Boolean.FALSE)
                    .build())
                .collect(Collectors.toList());

            savedSubGoals = subGoalRepository.saveAll(subGoals);
        }

        List<SubGoalResponse> subGoalResponses = savedSubGoals.stream()
            .map(savedSubGoal -> SubGoalResponse.builder()
                .subGoalId(savedSubGoal.getSubGoalId())
                .subGoalName(savedSubGoal.getSubGoalName())
                .color(savedSubGoal.getColor().getHexValue())
                .isStore(savedSubGoal.getIsStore())
                .build())
            .collect(Collectors.toList());

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
