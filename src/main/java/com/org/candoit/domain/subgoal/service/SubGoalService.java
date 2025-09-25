package com.org.candoit.domain.subgoal.service;

import com.org.candoit.domain.dailyaction.dto.DailyActionInfoWithAttainmentResponse;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionCustomRepository;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyprogress.dto.DailyProgressResponse;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressCustomRepository;
import com.org.candoit.domain.dailyprogress.service.DailyProgressService;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.CreatedSubGoalResponse;
import com.org.candoit.domain.subgoal.dto.DetailSubGoalResponse;
import com.org.candoit.domain.subgoal.dto.SimpleInfoWithAttainmentResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalPreviewResponse;
import com.org.candoit.domain.subgoal.dto.UpdateSubGoalRequest;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.dto.DateUnit;
import com.org.candoit.domain.subprogress.dto.Direction;
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
    private final DailyProgressService dailyProgressService;
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
        DateUnit unit) {
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

        return DetailSubGoalResponse.builder()
            .subGoal(subGoalPreviewResponse)
            .dailyActions(dailyActions)
            .progress(
                dailyProgressService.getCalendar(subGoalId, LocalDate.now(clock), Direction.CURRENT,
                    unit, dailyActions))
            .build();
    }
}
