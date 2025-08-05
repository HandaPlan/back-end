package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.subgoal.dto.SubGoalPreviewResponse;
import com.org.candoit.domain.subprogress.dto.SubProgressOverviewResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainGoalDetailsResponse {

    private SimpleMainGoalWithStatusResponse mainGoal;
    private List<SubGoalPreviewResponse> subGoals;
    private SubProgressOverviewResponse progress;
}
