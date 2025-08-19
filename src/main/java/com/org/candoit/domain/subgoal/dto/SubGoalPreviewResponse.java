package com.org.candoit.domain.subgoal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubGoalPreviewResponse {

    private Long id;
    private String name;
    private Integer slotNum;
    private Boolean attainment;
}
