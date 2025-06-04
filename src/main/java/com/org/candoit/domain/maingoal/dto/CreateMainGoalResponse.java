package com.org.candoit.domain.maingoal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMainGoalResponse {
    private Long mainGoalId;
    private String mainGoalName;
}
