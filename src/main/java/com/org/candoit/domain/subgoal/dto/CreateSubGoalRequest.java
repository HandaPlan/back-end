package com.org.candoit.domain.subgoal.dto;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateSubGoalRequest {

    private String name;
    private List<CreateDailyActionRequest> dailyActions;
}
