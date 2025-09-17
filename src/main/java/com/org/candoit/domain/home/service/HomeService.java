package com.org.candoit.domain.home.service;

import com.org.candoit.domain.home.dto.HomeOverallResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalDataForHomeResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.DetailSubProgressResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalDataForHomeResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subprogress.dto.CheckedSubProgressResponse;
import com.org.candoit.domain.subprogress.dto.DateUnit;
import com.org.candoit.domain.subprogress.dto.Direction;
import com.org.candoit.domain.subprogress.service.SubProgressService;
import com.org.candoit.global.response.CustomException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private static final Logger log = LoggerFactory.getLogger(HomeService.class);
    private final MainGoalCustomRepository mainGoalCustomRepository;
    private final SubGoalCustomRepository subGoalCustomRepository;
    private final Clock clock;
    private final SubProgressService subProgressService;

    public HomeOverallResponse getHomeOverall(Member member, Long mainGoalId) {

        // 조회하려는 메인골 정하는 로직
        MainGoal mainGoal = getMainGoal(member.getMemberId(), mainGoalId);

        LocalDate today = LocalDate.now(clock);

        List<SubGoal> subGoals = subGoalCustomRepository
            .findByMainGoalId(mainGoal.getMainGoalId())
            .stream()
            .sorted(Comparator.comparing(SubGoal::getSlotNum))
            .toList();

        List<SubGoalDataForHomeResponse> subGoalResponse =
            subGoals.isEmpty() ? List.of() : subGoals.stream().map(
                    subGoal -> SubGoalDataForHomeResponse.builder()
                        .id(subGoal.getSubGoalId())
                        .name(subGoal.getSubGoalName())
                        .slotNum(subGoal.getSlotNum())
                        .achievement(subGoal.getIsStore())
                        .build())
                .toList();

        List<DetailSubProgressResponse> lastChecked = subProgressService.getProgress(subGoals,
            today, Direction.PREV).getSubProgress();

        List<DetailSubProgressResponse> thisChecked = subProgressService.getProgress(subGoals,
            today, Direction.CURRENT).getSubProgress();

        MainGoalDataForHomeResponse mainGoalResponse = MainGoalDataForHomeResponse.builder()
            .id(mainGoal.getMainGoalId())
            .name(mainGoal.getMainGoalName())
            .lastAchievement(getRateForMainGoal(lastChecked))
            .thisAchievement(getRateForMainGoal(thisChecked))
            .build();

        List<CheckedSubProgressResponse> progress = subProgressService.getCheckedProgress(member,
            mainGoal.getMainGoalId(), today, Direction.CURRENT, DateUnit.WEEK);

        return HomeOverallResponse.builder()
            .mainGoal(mainGoalResponse)
            .subGoals(subGoalResponse)
            .progress(progress)
            .build();
    }

    private MainGoal getMainGoal(Long memberId, Long mainGoalId) {

        if (mainGoalId != null) {
            return mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId, memberId)
                .orElseThrow(() -> new CustomException(
                    MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));
        }
        return mainGoalCustomRepository
            .findRepresentativeMainGoalByMemberId(memberId)
            .orElseGet(() -> mainGoalCustomRepository.findOldestByMemberId(memberId)
                .orElseThrow(() -> new CustomException(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL)));
    }

    private int getRateForMainGoal(List<DetailSubProgressResponse> checkState) {
        if (checkState.isEmpty()) {
            return 0;
        }
        int sum = 0;
        int count = 0;

        for (DetailSubProgressResponse detailSubProgressResponse : checkState) {

            sum += detailSubProgressResponse.getRate();
            count++;
        }
        return (int) Math.round(sum * 1.0 / count);
    }
}
