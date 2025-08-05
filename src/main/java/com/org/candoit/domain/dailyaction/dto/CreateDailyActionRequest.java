package com.org.candoit.domain.dailyaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateDailyActionRequest {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull @Min(1) @Max(7)
    private Integer targetNum;
}
