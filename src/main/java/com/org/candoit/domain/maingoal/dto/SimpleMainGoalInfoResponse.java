package com.org.candoit.domain.maingoal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMainGoalInfoResponse {
    private Long id;
    private String name;
}
