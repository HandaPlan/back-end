package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreviewMainGoalResponse {

    private Long mainGoalId;
    private String mainGoalName;
    private MainGoalStatus mainGoalStatus;
}
