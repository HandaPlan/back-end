package com.org.candoit.domain.subgoal.dto;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateSubGoalForMainGoalRequest {
    @NotBlank
    private String name;
    private List<CreateDailyActionRequest> dailyActions;
}
