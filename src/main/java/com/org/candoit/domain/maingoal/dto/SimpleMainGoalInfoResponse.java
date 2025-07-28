package com.org.candoit.domain.maingoal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimpleMainGoalInfoResponse {
    private Long id;
    private String name;
}
