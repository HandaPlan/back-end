package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateMainGoalRequest {

    private String mainGoalName;
    private MainGoalStatus mainGoalStatus;
}
