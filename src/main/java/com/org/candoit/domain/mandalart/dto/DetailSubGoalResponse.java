package com.org.candoit.domain.mandalart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailSubGoalResponse {
    private String name;
    private List<DetailDailyActionResponse> dailyActions;
}
