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
import com.org.candoit.domain.subgoal.dto.CreateSubGoalForMainGoalRequest;
import com.org.candoit.domain.subgoal.dto.SubGoalPreviewResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.dto.Direction;
import com.org.candoit.domain.subprogress.service.SubProgressService;
import com.org.candoit.global.response.CustomException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

        List<CreateSubGoalForMainGoalRequest> requestSubGoals =
            Optional.ofNullable(request.getSubGoals()).orElseGet(Collections::emptyList);

        if (!requestSubGoals.isEmpty()) {

            List<SubGoal> subGoals = IntStream.range(0, request.getSubGoals().size())
                .mapToObj(i -> SubGoal.builder()
                    .mainGoal(savedMainGoal)
                    .subGoalName(request.getSubGoals().get(i).getName())
                    .slotNum(i + 1)
                    .isStore(Boolean.FALSE)
                    .build())
                .collect(Collectors.toList());

            List<SubGoal> savedSubGoals = subGoalRepository.saveAll(subGoals);

            List<DailyAction> dailyActions = IntStream.range(0, requestSubGoals.size())
                .boxed()
                .flatMap(i -> {
                    SubGoal nowSavedSubGoal = savedSubGoals.get(i);
                    List<CreateDailyActionRequest> requestDailyActions =
                        Optional.ofNullable(requestSubGoals.get(i)
                            .getDailyActions()).orElseGet(Collections::emptyList);

                    return requestDailyActions.stream()
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

        // 바꾸려는 메인골
        MainGoal target = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId, loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        // 현재 대표 메인골
        MainGoal currentRep = mainGoalCustomRepository.findRepresentativeMainGoalByMemberId(loginMember.getMemberId()).orElse(null);

        if (Boolean.TRUE.equals(target.getIsRepresentative())) {
            target.uncheckRepresentation();
        } else {
            if (currentRep != null && !currentRep.getMainGoalId().equals(target.getMainGoalId())) {
                currentRep.uncheckRepresentation();
            }
            target.checkRepresentation();
        }
        return Boolean.TRUE;
    }

    public SimpleMainGoalWithStatusResponse updateMainGoal(Member loginMember, Long mainGoalId,
        UpdateMainGoalRequest updateMainGoalRequest) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoal.updateMainGoal(updateMainGoalRequest.getName(),
            updateMainGoalRequest.getStatus());

        return new SimpleMainGoalWithStatusResponse(mainGoal.getMainGoalId(),
            mainGoal.getMainGoalName(), mainGoal.getMainGoalStatus());
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
