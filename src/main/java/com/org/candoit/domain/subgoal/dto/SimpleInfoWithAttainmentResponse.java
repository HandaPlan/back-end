package com.org.candoit.domain.subgoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleInfoWithAttainmentResponse {
    private Long id;
    private String name;
    private Boolean attainment;
}
