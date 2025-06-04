package com.org.candoit.domain.dailyaction.dto;

import lombok.Getter;

@Getter
public class CreateDailyActionRequest {

    private String title;
    private String content;
    private Integer targetNum;
}
