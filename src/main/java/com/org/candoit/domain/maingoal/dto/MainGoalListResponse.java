package com.org.candoit.domain.maingoal.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MainGoalListResponse {

    private List<MainGoalListCompositionResponse> mainGoals;
}
