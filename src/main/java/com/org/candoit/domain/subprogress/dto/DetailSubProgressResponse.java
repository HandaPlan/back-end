package com.org.candoit.domain.subprogress.dto;

import com.org.candoit.domain.subgoal.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailSubProgressResponse {

    private String subGoalName;
    private String color;
    private Integer rate;
}
