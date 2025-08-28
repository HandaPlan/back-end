package com.org.candoit.domain.subprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubProgressCalDto {
    private Long dailyActionId;
    private Integer targetNum;
    private Integer progressCount;
}
