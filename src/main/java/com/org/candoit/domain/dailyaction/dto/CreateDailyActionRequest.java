package com.org.candoit.domain.dailyaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateDailyActionRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull @Min(1) @Max(7)
    private Integer targetNum;
}
