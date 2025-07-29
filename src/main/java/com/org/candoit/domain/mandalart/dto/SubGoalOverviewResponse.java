package com.org.candoit.domain.mandalart.dto;

import com.org.candoit.domain.maingoal.dto.SimpleMainGoalInfoResponse;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubGoalOverviewResponse {
    private SimpleMainGoalInfoResponse mainGoal;
    private List<SimpleSubGoalInfoResponse> subGoals;
}
