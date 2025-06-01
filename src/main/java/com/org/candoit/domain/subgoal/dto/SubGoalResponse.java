package com.org.candoit.domain.subgoal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubGoalResponse {

    private Long subGoalId;
    private String subGoalName;
    private Boolean isStore;
    private String color;
}
