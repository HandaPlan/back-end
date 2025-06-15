package com.org.candoit.domain.maingoal.repository;

import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import java.util.List;
import java.util.Optional;

public interface MainGoalCustomRepository {

    Optional<MainGoal> findByMainGoalIdAndMemberId(Long mainGoalId, Long memberId);
    Optional<MainGoal> findRepresentativeMainGoalByMemberId(Long memberId);
    List<MainGoal> findByMemberIdAndStatus(Long memberId, MainGoalStatus status);
}
