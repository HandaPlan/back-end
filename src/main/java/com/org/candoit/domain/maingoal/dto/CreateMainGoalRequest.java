package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMainGoalRequest {
    private String mainGoalName;
    private List<CreateSubGoalRequest> subGoals;
}
