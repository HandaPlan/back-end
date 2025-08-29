package com.org.candoit.domain.subgoal.dto;

import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyprogress.dto.DailyProgressResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DetailSubGoalResponse {
    private SubGoalPreviewResponse subGoal;
    private List<DailyActionInfoWithAttainmentResponse> dailyActions;
    private DailyProgressResponse progress;
}
