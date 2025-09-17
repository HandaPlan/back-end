package com.org.candoit.domain.subgoal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubGoalDataForHomeResponse {

    private Long id;
    private String name;
    private Integer slotNum;
    private Boolean achievement;
}
