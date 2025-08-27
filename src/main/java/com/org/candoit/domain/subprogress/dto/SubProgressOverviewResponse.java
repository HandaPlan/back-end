package com.org.candoit.domain.subprogress.dto;

import com.org.candoit.domain.subgoal.dto.DetailSubProgressResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubProgressOverviewResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer weekOfMonth;
    private List<DetailSubProgressResponse> subProgress;
}
