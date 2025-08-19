package com.org.candoit.domain.subprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailSubProgressResponse {

    private String subGoalName;
    private Integer slotNum;
    private Integer rate;
}
