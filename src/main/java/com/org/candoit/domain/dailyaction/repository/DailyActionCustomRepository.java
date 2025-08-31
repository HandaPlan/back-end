package com.org.candoit.domain.dailyaction.repository;

import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import java.util.List;
import java.util.Optional;

public interface DailyActionCustomRepository {

    List<DailyAction> findByMemberIdAndSubGoalId(Long memberId, Long subGoalId);
    List<DailyActionInfoWithAttainmentResponse> getSimpleDailyActionInfo(Long subGoalId);
    Optional<DailyAction> findByMemberIdAndDailyActionId(Long memberId, Long dailyActionId);
}
