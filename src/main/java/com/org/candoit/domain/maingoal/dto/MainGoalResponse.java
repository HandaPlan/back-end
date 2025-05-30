package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainGoalResponse {

    private Long mainGoalId;
    private String mainGoalName;
    private Boolean isRepresentative;
    private MainGoalStatus mainGoalStatus;
}
