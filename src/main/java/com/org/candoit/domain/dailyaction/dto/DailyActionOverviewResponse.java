package com.org.candoit.domain.dailyaction.dto;

import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DailyActionOverviewResponse {

    private SimpleSubGoalInfoResponse subGoal;
    private List<SimpleDailyActionInfoResponse> dailyActions;
}
