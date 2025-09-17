package com.org.candoit.domain.home.dto;

import com.org.candoit.domain.maingoal.dto.MainGoalDataForHomeResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalDataForHomeResponse;
import com.org.candoit.domain.subprogress.dto.CheckedSubProgressResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeOverallResponse {

    private MainGoalDataForHomeResponse mainGoal;
    private List<SubGoalDataForHomeResponse> subGoals;
    private List<CheckedSubProgressResponse> progress;
}
