package com.org.candoit.domain.dailyprogress.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DetailProgressResponse {
    private Long dailyActionId;
    private String title;
    private String content;
    private List<LocalDate> checkedDate;
}
