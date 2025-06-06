package com.org.candoit.domain.maingoal.service;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.dto.CreateMainGoalRequest;
import com.org.candoit.domain.maingoal.dto.CreateMainGoalResponse;
import com.org.candoit.domain.maingoal.dto.MainGoalResponse;
import com.org.candoit.domain.maingoal.dto.PreviewMainGoalResponse;
import com.org.candoit.domain.maingoal.dto.UpdateMainGoalRequest;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.SubGoalResponse;
import com.org.candoit.domain.subgoal.entity.Color;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.global.response.CustomException;
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

            List<CreateSubGoalRequest> subGoalRequest = request.getSubGoals();

            List<DailyAction> dailyActions = IntStream.range(0, subGoalRequest.size())
                .boxed()
                .flatMap(i -> {
                    SubGoal nowSavedSubGoal = savedSubGoals.get(i);
                    List<CreateDailyActionRequest> dailyActionRequests = subGoalRequest.get(i).getDailyActions();
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

        return CreateMainGoalResponse.builder()
            .mainGoalName(savedMainGoal.getMainGoalName())
            .mainGoalId(savedMainGoal.getMainGoalId())
            .build();
    }

    public Boolean deleteMainGoal(Member loginMember, Long mainGoalId) {
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId, loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoalRepository.delete(mainGoal);
        return Boolean.TRUE;
    }

    public Boolean updateMainGoalRep(Member loginMember, Long mainGoalId){

        checkedAlreadyRep(loginMember.getMemberId());
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId, loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));
        mainGoal.checkRepresentation();

        return Boolean.TRUE;
    }

    private void checkedAlreadyRep(Long memberId) {
        MainGoal mainGoal = mainGoalCustomRepository.findRepresentativeMainGoalByMemberId(memberId).orElse(null);

        if (mainGoal != null) {
            uncheckRepresentative(mainGoal);
        }
    }

    private void uncheckRepresentative(MainGoal mainGoal) {
        mainGoal.uncheckRepresentation();
    }

    public MainGoalResponse updateMainGoal(Member loginMember, Long mainGoalId, UpdateMainGoalRequest updateMainGoalRequest){
        MainGoal mainGoal = mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoalId, loginMember.getMemberId())
            .orElseThrow(() -> new CustomException(
                MainGoalErrorCode.NOT_FOUND_MAIN_GOAL));

        mainGoal.updateMainGoal(updateMainGoalRequest.getMainGoalName(), updateMainGoalRequest.getMainGoalStatus());

        return MainGoalResponse.builder()
            .mainGoalId(mainGoal.getMainGoalId())
            .mainGoalStatus(mainGoal.getMainGoalStatus())
            .mainGoalName(mainGoal.getMainGoalName())
            .isRepresentative(mainGoal.getIsRepresentative())
            .subGoals(createSubGoalResponse(mainGoal.getSubGoals()))
            .build();
    }

    private List<SubGoalResponse> createSubGoalResponse(List<SubGoal> subGoalList){
        return subGoalList.stream()
            .map(subGoal -> SubGoalResponse.builder()
                .subGoalId(subGoal.getSubGoalId())
                .subGoalName(subGoal.getSubGoalName())
                .color(subGoal.getColor().getHexValue())
                .isStore(subGoal.getIsStore())
                .build())
            .collect(Collectors.toList());
    }

    public List<PreviewMainGoalResponse> getPreviewList(Member member, MainGoalStatus state) {

       return mainGoalCustomRepository.findByMemberIdAndStatus(member.getMemberId(), state)
           .stream()
           .map(mainGoal -> PreviewMainGoalResponse.builder()
               .mainGoalId(mainGoal.getMainGoalId())
               .mainGoalName(mainGoal.getMainGoalName())
               .mainGoalStatus(mainGoal.getMainGoalStatus())
               .build())
           .collect(Collectors.toList());
    }
}
