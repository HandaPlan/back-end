package com.org.candoit.domain.subgoal.dto;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateSubGoalRequest {

    private String subGoalName;
    private List<CreateDailyActionRequest> dailyActions;
}
