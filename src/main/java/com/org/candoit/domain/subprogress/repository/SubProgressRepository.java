package com.org.candoit.domain.subprogress.repository;

import com.org.candoit.domain.subprogress.entity.SubProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubProgressRepository extends JpaRepository<SubProgress, Long> {
    public SubProgress findBySubGoal_SubGoalId(Long subGoalId);
}
