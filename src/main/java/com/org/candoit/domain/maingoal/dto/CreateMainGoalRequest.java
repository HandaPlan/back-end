package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.subgoal.dto.CreateSubGoalForMainGoalRequest;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMainGoalRequest {

    @NotBlank
    private String name;
    private List<CreateSubGoalForMainGoalRequest> subGoals;
}
