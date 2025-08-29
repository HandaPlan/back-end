package com.org.candoit.domain.dailyprogress.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DailyProgressResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> dailyProgress;
}
