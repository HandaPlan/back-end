package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateMainGoalRequest {
    @NotBlank
    private String mainGoalName;
    @NotBlank
    private MainGoalStatus mainGoalStatus;
}
