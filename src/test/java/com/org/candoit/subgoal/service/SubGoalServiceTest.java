package com.org.candoit.subgoal.service;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subgoal.service.SubGoalService;
import com.org.candoit.domain.subprogress.entity.SubProgress;
import com.org.candoit.domain.subprogress.repository.SubProgressRepository;
import com.org.candoit.global.response.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubGoalServiceTest {

    @Mock
    SubGoalRepository subGoalRepository;
    @Mock
    MainGoalCustomRepository mainGoalCustomRepository;
    @Mock
    SubProgressRepository subProgressRepository;
    @Mock
    DailyProgressRepository dailyProgressRepository;
    @Mock
    DailyActionRepository dailyActionRepository;

    @InjectMocks
    SubGoalService subGoalService;

    Member loginMember;

    @BeforeEach
    void setUp() {
        loginMember = Member.builder()
            .memberId(1l)
            .build();
    }

    @Test
    @DisplayName("데일리 액션이 없을 때, 서브골 저장 성공")
    void createSubGoal_success_withoutDailyAction() {
        // given
        List<SubGoal> alreadySavedSubGoals = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            alreadySavedSubGoals.add(SubGoal.builder().subGoalId(i).build());
        }
        MainGoal mainGoal = MainGoal.builder()
            .mainGoalId(1l)
            .subGoals(alreadySavedSubGoals)
            .build();

        when(mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoal.getMainGoalId(),
            loginMember.getMemberId())).thenReturn(
            Optional.of(mainGoal));

        CreateSubGoalRequest request = new CreateSubGoalRequest("subGoal", List.of());
        SubGoal create = SubGoal.builder()
            .subGoalName(request.getName())
            .slotNum(alreadySavedSubGoals.size() + 1)
            .build();

        when(subGoalRepository.save(any(SubGoal.class))).thenReturn(create);

        // when
        SimpleSubGoalInfoResponse result = subGoalService.createSubGoal(loginMember, mainGoal.getMainGoalId(), request);

        // then
        assertEquals(request.getName(), result.getName());
        verify(subProgressRepository).save(any(SubProgress.class));
        verifyNoInteractions(dailyActionRepository, dailyProgressRepository);
    }

    @Test
    @DisplayName("일치하는 메인골이 없어 서브골 생성에 실패한다.")
    void givenNotMatchMainGoalId_whenCreateSubGoal_thenThrowNotFoundException() {
        // given
        when(mainGoalCustomRepository.findByMainGoalIdAndMemberId(anyLong(), anyLong())).thenReturn(
            Optional.empty());
        CreateSubGoalRequest request = new CreateSubGoalRequest("subGoal", List.of());

        // when, then
        assertThatThrownBy(() -> subGoalService.createSubGoal(loginMember, 1l, request))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL.getMessage())
            .extracting("errorCode")
            .isEqualTo(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL);

    }

}
