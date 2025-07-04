package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMainGoalRequest {

    @NotBlank
    private String name;
    private List<CreateSubGoalRequest> subGoals;
}
