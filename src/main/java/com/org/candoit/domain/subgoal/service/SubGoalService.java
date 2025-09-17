package com.org.candoit.domain.subgoal.service;

import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.dto.SimpleDailyActionInfoResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionCustomRepository;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyprogress.dto.DailyProgressResponse;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressCustomRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.CreatedSubGoalResponse;
import com.org.candoit.domain.subgoal.dto.DetailSubGoalResponse;
import com.org.candoit.domain.subgoal.dto.SimpleInfoWithAttainmentResponse;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalPreviewResponse;
import com.org.candoit.domain.subgoal.dto.UpdateSubGoalRequest;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.global.response.CustomException;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SubGoalService {

    private final SubGoalRepository subGoalRepository;
    private final SubGoalCustomRepository subGoalCustomRepository;
    private final MainGoalCustomRepository mainGoalCustomRepository;
    private final DailyActionRepository dailyActionRepository;
    private final DailyActionCustomRepository dailyActionCustomRepository;
    private final Clock clock;
    private final DailyProgressCustomRepository dailyProgressCustomRepository;

    public CreatedSubGoalResponse createSubGoal(Member loginMember, Long mainGoalId,
        CreateSubGoalRequest createSubGoalRequest) {

        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
            loginMember.getMemberId()).orElseThrow(() -> new CustomException(
            MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        SubGoal subGoal = SubGoal.builder()
            .mainGoal(mainGoal)
            .subGoalName(createSubGoalRequest.getName())
            .isStore(Boolean.FALSE)
            .slotNum(createSubGoalRequest.getSlotNum())
            .build();

        SubGoal savedSubGoal = subGoalRepository.save(subGoal);

        if (!createSubGoalRequest.getDailyActions().isEmpty()) {
            List<DailyAction> dailyActions = createSubGoalRequest.getDailyActions().stream()
                .map(dailyAction ->
                    DailyAction.builder()
                        .dailyActionTitle(dailyAction.getTitle())
                        .content(dailyAction.getContent())
                        .subGoal(savedSubGoal)
                        .targetNum(dailyAction.getTargetNum())
                        .isStore(Boolean.FALSE)
                        .build())
                .collect(Collectors.toList());

            dailyActionRepository.saveAll(dailyActions);
        }

        return new CreatedSubGoalResponse(savedSubGoal.getSubGoalId(),
            savedSubGoal.getSubGoalName(), savedSubGoal.getSlotNum());
    }

    public SimpleInfoWithAttainmentResponse updateSubGoal(Member loginMember, Long subGoalId,
        UpdateSubGoalRequest updateSubGoalRequest) {

        SubGoal updateSubgoal = subGoalCustomRepository.findByMemberIdAndSubGoalId(
                loginMember.getMemberId(), subGoalId)
            .orElseThrow(() -> new CustomException(SubGoalErrorCode.NOT_FOUND_SUB_GOAL));

        updateSubgoal.changeSubGoalProperty(updateSubGoalRequest.getName(),
            updateSubGoalRequest.getAttainment());
        return new SimpleInfoWithAttainmentResponse(updateSubgoal.getSubGoalId(),
            updateSubgoal.getSubGoalName(), updateSubGoalRequest.getAttainment());
    }

    public Boolean deleteSubGoal(Member loginMember, Long subGoalId) {
        subGoalCustomRepository.findByMemberIdAndSubGoalId(loginMember.getMemberId(), subGoalId)
            .orElseThrow(() -> new CustomException(SubGoalErrorCode.NOT_FOUND_SUB_GOAL));
        subGoalRepository.deleteById(subGoalId);
        return Boolean.TRUE;
    }

    public DetailSubGoalResponse getDetailSubGoal(Member loginMember, Long subGoalId,
        String period) {
        SubGoal subGoal = subGoalCustomRepository.findByMemberIdAndSubGoalId(
                loginMember.getMemberId(), subGoalId)
            .orElseThrow(() -> new CustomException(SubGoalErrorCode.NOT_FOUND_SUB_GOAL));
        SubGoalPreviewResponse subGoalPreviewResponse = SubGoalPreviewResponse.builder()
            .id(subGoal.getSubGoalId())
            .name(subGoal.getSubGoalName())
            .slotNum(subGoal.getSlotNum())
            .attainment(subGoal.getIsStore())
            .build();
        List<DailyActionInfoWithAttainmentResponse> dailyActions = dailyActionCustomRepository.getSimpleDailyActionInfo(
            subGoalId);

        DailyProgressResponse dailyProgressResponse;

        LocalDate now = LocalDate.now(clock);
        LocalDate startDay = LocalDate.now(clock);
        LocalDate endDay = LocalDate.now(clock);

        if ("week".equals(period)) {
            startDay = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            endDay = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        } else if ("month".equals(period)) {
            startDay = now.with(TemporalAdjusters.firstDayOfMonth());
            endDay = now.with(TemporalAdjusters.lastDayOfMonth());
        }

        // 데일리 액션이 없는 경우
        if (dailyActions.isEmpty()) {
            dailyActions = List.of();
            dailyProgressResponse = new DailyProgressResponse(startDay, endDay, List.of());
        }
        // 데일리 액션이 있는 경우
        else{
            List<LocalDate> checkedDate = dailyProgressCustomRepository.distinctCheckedDate(
                subGoalId, startDay, endDay);

            dailyProgressResponse = DailyProgressResponse.builder()
                .startDate(startDay)
                .endDate(endDay)
                .dailyProgress(checkedDate)
                .build();
        }
        return DetailSubGoalResponse.builder()
            .subGoal(subGoalPreviewResponse)
            .dailyActions(dailyActions)
            .progress(dailyProgressResponse)
            .build();
    }
}
