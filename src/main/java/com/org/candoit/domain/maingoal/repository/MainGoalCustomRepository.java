package com.org.candoit.domain.maingoal.repository;

import com.org.candoit.domain.maingoal.dto.SimpleMainGoalInfoResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import java.util.List;
import java.util.Optional;

public interface MainGoalCustomRepository {

    Optional<MainGoal> findByMainGoalIdAndMemberId(Long mainGoalId, Long memberId);

    Optional<MainGoal> findActiveMainGoal(Long mainGoalId, Long memberId);

    Optional<MainGoal> findRepresentativeMainGoalByMemberId(Long memberId);

    List<MainGoal> findByMemberIdAndStatus(Long memberId, MainGoalStatus status);

    List<SimpleMainGoalInfoResponse> searchActiveMainGoals(Long memberId);

    List<MainGoal> findByMemberId(Long memberId);

    Optional<MainGoal> findOldestByMemberId(Long memberId);

    Boolean existsActiveMainGoal(Long memberId);
}
