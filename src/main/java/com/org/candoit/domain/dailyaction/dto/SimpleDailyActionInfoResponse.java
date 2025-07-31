package com.org.candoit.domain.dailyaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleDailyActionInfoResponse {

    private Long id;
    private String title;
    private String content;
    private Integer targetNum;
}
