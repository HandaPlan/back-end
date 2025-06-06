package com.org.candoit.domain.mandalart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailDailyActionResponse {
    private String title;
    private String content;
    private Integer targetNum;
}
