package com.org.candoit.domain.mandalart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailMainGoalResponse {
    private String name;
    private List<DetailSubGoalResponse> subGoals;
}
