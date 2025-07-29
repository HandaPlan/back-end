package com.org.candoit.domain.mandalart.dto;

import com.org.candoit.domain.maingoal.dto.SimpleMainGoalInfoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MainGoalOverviewResponse {
    private List<SimpleMainGoalInfoResponse> mainGoals;
}
