package com.org.candoit.domain.maingoal.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum MainGoalStatus {
    ACTIVITY,
    ATTAINMENT,
    PAUSE;

    @JsonCreator
    public static MainGoalStatus from(String input) {
        return MainGoalStatus.valueOf(input.toUpperCase());
    }
}
