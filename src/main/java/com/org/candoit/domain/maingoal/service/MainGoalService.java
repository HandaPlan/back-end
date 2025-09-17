package com.org.candoit.domain.maingoal.service;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.MainGoalDetailsResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalListCompositionResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalListResponse;
import com.org.candoit.domain.maingoal.dto.SimpleMainGoalInfoResponse;
import com.org.candoit.domain.maingoal.dto.SimpleMainGoalWithStatusResponse;
import com.org.candoit.domain.maingoal.dto.UpdateMainGoalRequest;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.DetailSubProgressResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalPreviewResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.dto.Direction;
import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import com.org.candoit.domain.subprogress.dto.SubProgressOverviewResponse;
import com.org.candoit.domain.subprogress.repository.SubProgressQueryRepository;
import com.org.candoit.domain.subprogress.service.SubProgressService;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.util.DateTimeUtil;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MainGoalService {

    private final MainGoalRepository mainGoalRepository;
    private final MainGoalCustomRepository mainGoalCustomRepository;
    private final SubGoalRepository subGoalRepository;
    private final DailyActionRepository dailyActionRepository;
    private final SubGoalCustomRepository subGoalCustomRepository;
    private final SubProgressQueryRepository subProgressQueryRepository;
    private final SubProgressService subProgressService;
    private final Clock clock;

    public SimpleMainGoalInfoResponse createMainGoal(Member member, CreateMainGoalRequest request) {

        MainGoal mainGoal = MainGoal.builder()
            .member(member)
            .mainGoalName(request.getName())
            .mainGoalStatus(MainGoalStatus.ACTIVITY)
            .isRepresentative(Boolean.FALSE)
            .build();

        MainGoal savedMainGoal = mainGoalRepository.save(mainGoal);

        List<SubGoal> savedSubGoals;

        if (!request.getSubGoals().isEmpty()) {

            List<SubGoal> subGoals = IntStream.range(0, request.getSubGoals().size())
                .mapToObj(i -> SubGoal.builder()
                    .mainGoal(savedMainGoal)
                    .subGoalName(request.getSubGoals().get(i).getName())
                    .slotNum(i + 1)
                    .isStore(Boolean.FALSE)
                    .build())
                .collect(Collectors.toList());

            savedSubGoals = subGoalRepository.saveAll(subGoals);

            List<CreateSubGoalRequest> subGoalRequest = request.getSubGoals();

            List<DailyAction> dailyActions = IntStream.range(0, subGoalRequest.size())
                .boxed()
                .flatMap(i -> {
                    SubGoal nowSavedSubGoal = savedSubGoals.get(i);
                    List<CreateDailyActionRequest> dailyActionRequests = subGoalRequest.get(i)
                        .getDailyActions();
                    return dailyActionRequests.stream()
                        .map(req -> DailyAction.builder()
                            .dailyActionTitle(req.getTitle())
                            .content(req.getContent())
                            .subGoal(nowSavedSubGoal)
                            .targetNum(req.getTargetNum())
                            .isStore(Boolean.FALSE)
                            .build());
                }).collect(Collectors.toList());

            dailyActionRepository.saveAll(dailyActions);
        }

        return new SimpleMainGoalInfoResponse(savedMainGoal.getMainGoalId(),
            savedMainGoal.getMainGoalName());
    }

    public Boolean deleteMainGoal(Member loginMember, Long mainGoalId) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoalRepository.deleteById(mainGoal.getMainGoalId());
        return Boolean.TRUE;
    }

    public Boolean updateMainGoalRep(Member loginMember, Long mainGoalId) {

        checkedAlreadyRep(loginMember.getMemberId());
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));
        mainGoal.checkRepresentation();

        return Boolean.TRUE;
    }

    private void checkedAlreadyRep(Long memberId) {
        MainGoal mainGoal = mainGoalCustomRepository.findRepresentativeMainGoalByMemberId(memberId)
            .orElse(null);

        if (mainGoal != null) {
            uncheckRepresentative(mainGoal);
        }
    }

    private void uncheckRepresentative(MainGoal mainGoal) {
        mainGoal.uncheckRepresentation();
    }

    public SimpleMainGoalWithStatusResponse updateMainGoal(Member loginMember, Long mainGoalId,
        UpdateMainGoalRequest updateMainGoalRequest) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoal.updateMainGoal(updateMainGoalRequest.getMainGoalName(),
            updateMainGoalRequest.getMainGoalStatus());

        return new SimpleMainGoalWithStatusResponse(mainGoal.getMainGoalId(),
            mainGoal.getMainGoalName(), mainGoal.getMainGoalStatus());
    }

    private List<SubGoalResponse> createSubGoalResponse(List<SubGoal> subGoalList) {
        return subGoalList.stream()
            .map(subGoal -> SubGoalResponse.builder()
                .subGoalId(subGoal.getSubGoalId())
                .subGoalName(subGoal.getSubGoalName())
                .slotNum(subGoal.getSlotNum())
                .attainment(subGoal.getIsStore())
                .build())
            .collect(Collectors.toList());
    }

    public MainGoalListResponse getPreviewList(Member member, MainGoalStatus state) {

        return MainGoalListResponse.builder()
            .mainGoals(mainGoalCustomRepository.findByMemberIdAndStatus(member.getMemberId(), state)
                .stream()
                .map(mainGoal -> MainGoalListCompositionResponse.builder()
                    .id(mainGoal.getMainGoalId())
                    .name(mainGoal.getMainGoalName())
                    .status(mainGoal.getMainGoalStatus())
                    .isRep(mainGoal.getIsRepresentative())
                    .build())
                .collect(Collectors.toList())).build();
    }

    public MainGoalDetailsResponse getMainGoalDetails(Member loginMember, Long mainGoalId) {

        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));
        List<SubGoal> subGoals = subGoalCustomRepository.findByMainGoalId(mainGoalId);
        List<SubGoalPreviewResponse> subGoalPreview = new ArrayList<>();

        SimpleMainGoalWithStatusResponse simpleMainGoalWithStatus = new SimpleMainGoalWithStatusResponse(
            mainGoal.getMainGoalId(), mainGoal.getMainGoalName(), mainGoal.getMainGoalStatus());
        subGoals.forEach(subGoal -> {
            subGoalPreview.add(SubGoalPreviewResponse.builder()
                .id(subGoal.getSubGoalId())
                .name(subGoal.getSubGoalName())
                .slotNum(subGoal.getSlotNum())
                .attainment(subGoal.getIsStore())
                .build());
        });

        return MainGoalDetailsResponse.builder()
            .mainGoal(simpleMainGoalWithStatus)
            .subGoals(subGoalPreview)
            .progress(
                subProgressService.getProgress(subGoals, LocalDate.now(clock), Direction.CURRENT))
            .build();
    }
}
