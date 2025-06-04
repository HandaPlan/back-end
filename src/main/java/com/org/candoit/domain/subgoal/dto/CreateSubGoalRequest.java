package com.org.candoit.domain.subgoal.dto;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateSubGoalRequest {

    private String name;
    private String color;
    private List<CreateDailyActionRequest> dailyActions;
}
