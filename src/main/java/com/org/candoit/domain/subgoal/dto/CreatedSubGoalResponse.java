package com.org.candoit.domain.subgoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatedSubGoalResponse {

    private Long id;
    private String name;
    private Integer slotNum;
}
