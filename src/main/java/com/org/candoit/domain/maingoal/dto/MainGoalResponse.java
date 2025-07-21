package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.subgoal.dto.SubGoalResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainGoalResponse {

    private Long mainGoalId;
    private String mainGoalName;
    private Boolean isRep;
    private MainGoalStatus mainGoalStatus;
    private List<SubGoalResponse> subGoals;
}
