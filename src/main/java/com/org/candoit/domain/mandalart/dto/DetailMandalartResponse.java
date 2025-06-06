package com.org.candoit.domain.mandalart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DetailMandalartResponse {
    List<DetailMainGoalResponse> mainGoals;
}
