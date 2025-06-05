package com.org.candoit.domain.mandalart.service;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.mandalart.dto.DetailDailyActionResponse;
import com.org.candoit.domain.mandalart.dto.DetailMainGoalResponse;
import com.org.candoit.domain.mandalart.dto.DetailMandalartResponse;
import com.org.candoit.domain.mandalart.dto.DetailSubGoalResponse;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MandalartService {

    private final SubGoalCustomRepository subGoalRepository;

    public DetailMandalartResponse getMandalart(Member loginMember) {

        List<SubGoal> subGoals = subGoalRepository.findByMemberId(loginMember.getMemberId());
        List<DetailMainGoalResponse> detailMainGoalResponses = new ArrayList<>();

        Map<Long, List<SubGoal>> byMainGoalId = subGoals.stream()
            .collect(Collectors.groupingBy(subGoal -> subGoal.getMainGoal().getMainGoalId()));

        byMainGoalId.forEach((mainGoalId, subGoalList)->{
            List<DetailSubGoalResponse> detailSubGoalResponses = new ArrayList<>();
           subGoalList.forEach(subGoal -> {
             List<DailyAction> dailyActions = subGoal.getDailyActions();
             List<DetailDailyActionResponse> detailDailyActionResponses = IntStream.range(0, dailyActions.size())
                 .boxed()
                 .map(i -> {
                     return DetailDailyActionResponse.builder()
                         .title(dailyActions.get(i).getDailyActionTitle())
                         .content(dailyActions.get(i).getContent())
                         .targetNum(dailyActions.get(i).getTargetNum())
                         .build();
                 }).collect(Collectors.toList());

             DetailSubGoalResponse detailSubGoalResponse = DetailSubGoalResponse.builder()
                 .name(subGoal.getSubGoalName())
                 .dailyActions(detailDailyActionResponses)
                 .build();

             detailSubGoalResponses.add(detailSubGoalResponse);
           });
           detailMainGoalResponses.add(DetailMainGoalResponse.builder()
               .name(subGoalList.get(0).getMainGoal().getMainGoalName())
               .subGoals(detailSubGoalResponses)
               .build());
        });

        return DetailMandalartResponse.builder()
            .mainGoals(detailMainGoalResponses)
            .build();
    }
}
