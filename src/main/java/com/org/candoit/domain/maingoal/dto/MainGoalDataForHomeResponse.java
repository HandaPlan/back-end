package com.org.candoit.domain.maingoal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainGoalDataForHomeResponse {

    private Long id;
    private String name;
    private Integer lastAchievement;
    private Integer thisAchievement;
}
