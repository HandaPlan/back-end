package com.org.candoit.domain.dailyaction.dto;

import lombok.Getter;

@Getter
public class CreateDailyActionRequest {

    private String dailyActionTitle;
    private String dailyActionContent;
    private Integer targetNum;
}
