package com.org.candoit.domain.subgoal.repository;

import com.org.candoit.domain.subgoal.entity.SubGoal;
import java.util.List;
import java.util.Optional;

public interface SubGoalCustomRepository {

    List<SubGoal> findByMemberId(Long memberId);
    List<SubGoal> findByMemberIdAndMainGoalId(Long memberId, Long mainGoalId);
    Optional<SubGoal> findByMemberIdAndSubGoalId(Long memberId, Long subGoalId);
}
