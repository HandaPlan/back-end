package com.org.candoit.domain.dailyaction.repository;

import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import java.util.List;

public interface DailyActionCustomRepository {

    List<DailyAction> findByMemberIdAndSubGoalId(Long memberId, Long subGoalId);
    List<SimpleDailyActionInfoResponse> getSimpleDailyActionInfo(Long subGoalId);
}
