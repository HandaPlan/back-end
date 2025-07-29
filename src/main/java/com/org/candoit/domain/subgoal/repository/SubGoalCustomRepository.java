package com.org.candoit.domain.subgoal.repository;

import com.org.candoit.domain.subgoal.entity.SubGoal;
import java.util.List;

public interface SubGoalCustomRepository {

    List<SubGoal> findByMemberId(Long memberId);
    List<SubGoal> findByMemberIdAndMainGoalId(Long memberId, Long mainGoalId);
}
