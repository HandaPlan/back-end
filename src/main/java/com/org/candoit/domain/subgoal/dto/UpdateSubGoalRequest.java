package com.org.candoit.domain.subgoal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UpdateSubGoalRequest {
    @NotNull
    private String name;
    @NotNull
    private Boolean attainment;
}
