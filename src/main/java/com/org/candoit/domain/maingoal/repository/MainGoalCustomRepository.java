package com.org.candoit.domain.maingoal.repository;

import com.org.candoit.domain.maingoal.entity.MainGoal;
import java.util.Optional;

public interface MainGoalCustomRepository {

    Optional<MainGoal> findByMainGoalIdAndMemberId(Long mainGoalId, Long memberId);
    Optional<MainGoal> findRepresentativeMainGoalByMemberId(Long memberId);
}
