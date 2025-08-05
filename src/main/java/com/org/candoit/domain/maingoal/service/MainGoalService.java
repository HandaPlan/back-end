package com.org.candoit.domain.maingoal.service;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyprogress.entity.DailyProgress;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressRepository;
import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.CreateMainGoalResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalDetailsResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalListCompositionResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalListResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalResponse;
import com.org.candoit.domain.maingoal.dto.SimpleMainGoalWithStatusResponse;
import com.org.candoit.domain.maingoal.dto.UpdateMainGoalRequest;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.SubGoalPreviewResponse;
import com.org.candoit.domain.subgoal.dto.SubGoalResponse;
import com.org.candoit.domain.subgoal.entity.Color;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.dto.DetailSubProgressResponse;
import com.org.candoit.domain.subprogress.dto.SubProgressOverviewResponse;
import com.org.candoit.domain.subprogress.entity.SubProgress;
import com.org.candoit.domain.subprogress.repository.SubProgressRepository;
import com.org.candoit.global.response.CustomException;
import com.org.candoit.global.util.DateTimeUtil;
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
    private final SubProgressRepository subProgressRepository;
    private final DailyProgressRepository dailyProgressRepository;

    public CreateMainGoalResponse createMainGoal(Member member, CreateMainGoalRequest request) {

        MainGoal mainGoal = MainGoal.builder()
            .member(member)
            .mainGoalName(request.getName())
            .mainGoalStatus(MainGoalStatus.ACTIVITY)
            .thisAchievementRate(0)
            .lastAchievementRate(0)
            .isRepresentative(Boolean.FALSE)
            .build();

        MainGoal savedMainGoal = mainGoalRepository.save(mainGoal);

        List<SubGoal> savedSubGoals;

        if (!request.getSubGoals().isEmpty()) {

            List<SubGoal> subGoals = IntStream.range(0, request.getSubGoals().size())
                .mapToObj(i -> SubGoal.builder()
                    .mainGoal(savedMainGoal)
                    .subGoalName(request.getSubGoals().get(i).getName())
                    .color(Color.getColor(i))
                    .isStore(Boolean.FALSE)
                    .build())
                .collect(Collectors.toList());

            savedSubGoals = subGoalRepository.saveAll(subGoals);

            List<SubProgress> subProgresses = savedSubGoals.stream()
                .map(subGoal -> SubProgress.builder().subGoal(subGoal).build())
                .collect(Collectors.toList());


            subProgressRepository.saveAll(subProgresses);

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

            List<DailyAction> savedDailyActions = dailyActionRepository.saveAll(dailyActions);

            List<DailyProgress> dailyProgresses = savedDailyActions.stream()
                .map(dailyAction -> DailyProgress.builder().dailyAction(dailyAction).build())
                .collect(Collectors.toList());

            dailyProgressRepository.saveAll(dailyProgresses);
        }

        return CreateMainGoalResponse.builder()
            .mainGoalName(savedMainGoal.getMainGoalName())
            .mainGoalId(savedMainGoal.getMainGoalId())
            .build();
    }

    public Boolean deleteMainGoal(Member loginMember, Long mainGoalId) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoalRepository.delete(mainGoal);
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

    public MainGoalResponse updateMainGoal(Member loginMember, Long mainGoalId,
        UpdateMainGoalRequest updateMainGoalRequest) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId,
                loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoal.updateMainGoal(updateMainGoalRequest.getMainGoalName(),
            updateMainGoalRequest.getMainGoalStatus());

        return MainGoalResponse.builder()
            .mainGoalId(mainGoal.getMainGoalId())
            .mainGoalStatus(mainGoal.getMainGoalStatus())
            .mainGoalName(mainGoal.getMainGoalName())
            .isRep(mainGoal.getIsRepresentative())
            .subGoals(createSubGoalResponse(mainGoal.getSubGoals()))
            .build();
    }

    private List<SubGoalResponse> createSubGoalResponse(List<SubGoal> subGoalList) {
        return subGoalList.stream()
            .map(subGoal -> SubGoalResponse.builder()
                .subGoalId(subGoal.getSubGoalId())
                .subGoalName(subGoal.getSubGoalName())
                .color(subGoal.getColor().getHexValue())
                .isStore(subGoal.getIsStore())
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
        List<DetailSubProgressResponse> detailSubProgress = new ArrayList<>();

        SimpleMainGoalWithStatusResponse simpleMainGoalWithStatus = new SimpleMainGoalWithStatusResponse(
            mainGoal.getMainGoalId(), mainGoal.getMainGoalName(), mainGoal.getMainGoalStatus());
        subGoals.forEach(subGoal -> {
            subGoalPreview.add(SubGoalPreviewResponse.builder()
                .id(subGoal.getSubGoalId())
                .name(subGoal.getSubGoalName())
                .color(subGoal.getColor().getHexValue())
                .attainment(subGoal.getIsStore())
                .build());

            SubProgress subProgress = subProgressRepository.findBySubGoal_SubGoalId(subGoal.getSubGoalId());
            Integer checked = subProgress.getCheckedCount();
            Integer target = subProgress.getTargetCount();

            int rate;
            if (checked == null || checked == 0 || target == null) {
                rate = 0;
            } else {
                rate = target / checked;
            }

            detailSubProgress.add(
                new DetailSubProgressResponse(
                    subGoal.getSubGoalName(),
                    subGoal.getColor().getHexValue(),
                    rate
                )
            );

        });

        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate friday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        int weekOfMonth = DateTimeUtil.getWeekOfMonth(now);

        SubProgressOverviewResponse subProgressOverview = SubProgressOverviewResponse.builder()
            .startDate(monday)
            .endDate(friday)
            .weekOfMonth(weekOfMonth)
            .subProgress(detailSubProgress)
            .build();

        return MainGoalDetailsResponse.builder()
            .mainGoal(simpleMainGoalWithStatus)
            .subGoals(subGoalPreview)
            .progress(subProgressOverview)
            .build();
    }
}
