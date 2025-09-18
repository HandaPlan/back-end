package com.org.candoit.subgoal.service;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.exception.MainGoalErrorCode;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.CreateSubGoalRequest;
import com.org.candoit.domain.subgoal.dto.CreatedSubGoalResponse;
import com.org.candoit.domain.subgoal.dto.SimpleInfoWithAttainmentResponse;
import com.org.candoit.domain.subgoal.dto.SimpleSubGoalInfoResponse;
import com.org.candoit.domain.subgoal.dto.UpdateSubGoalRequest;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.exception.SubGoalErrorCode;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subgoal.service.SubGoalService;
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
    SubGoalCustomRepository subGoalCustomRepository;
    @Mock
    MainGoalCustomRepository mainGoalCustomRepository;
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
            .build();

        when(mainGoalCustomRepository.findByMainGoalIdAndMemberId(mainGoal.getMainGoalId(),
            loginMember.getMemberId())).thenReturn(
            Optional.of(mainGoal));

        CreateSubGoalRequest request = new CreateSubGoalRequest("subGoal", 1, List.of());
        SubGoal create = SubGoal.builder()
            .subGoalName(request.getName())
            .slotNum(request.getSlotNum())
            .build();

        when(subGoalRepository.save(any(SubGoal.class))).thenReturn(create);

        // when
        CreatedSubGoalResponse result = subGoalService.createSubGoal(loginMember, mainGoal.getMainGoalId(), request);

        // then
        assertEquals(request.getName(), result.getName());
    }

    @Test
    @DisplayName("일치하는 메인골이 없어 서브골 생성에 실패한다.")
    void givenNotMatchMainGoalId_whenCreateSubGoal_thenThrowNotFoundException() {
        // given
        when(mainGoalCustomRepository.findByMainGoalIdAndMemberId(anyLong(), anyLong())).thenReturn(
            Optional.empty());
        CreateSubGoalRequest request = new CreateSubGoalRequest("subGoal", 1, List.of());

        // when, then
        assertThatThrownBy(() -> subGoalService.createSubGoal(loginMember, 1l, request))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL.getMessage())
            .extracting("errorCode")
            .isEqualTo(MainGoalErrorCode.NOT_FOUND_MAIN_GOAL);
    }

    @Test
    @DisplayName("서브골 이름, 달성 여부 수정 성공")
    void givenChangeNameAndAttainment_whenUpdateSubGoal_thenSuccess(){
        //given
        SubGoal originSubGoal = SubGoal.builder()
            .subGoalId(1l)
            .subGoalName("before change")
            .build();
        when(subGoalCustomRepository.findByMemberIdAndSubGoalId(anyLong(), anyLong())).thenReturn(Optional.of(originSubGoal));
        UpdateSubGoalRequest request = new UpdateSubGoalRequest("change complete", false);

        //when
        SimpleInfoWithAttainmentResponse result = subGoalService.updateSubGoal(loginMember, 1l, request);

        //then
        assertEquals(originSubGoal.getSubGoalId(), result.getId());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getAttainment(), result.getAttainment());
    }

    @Test
    @DisplayName("수정을 요청하는 서브골의 아이디가 DB 내에 존재하지 않아 예외가 발생한다.")
    void givenNotMatchSubGoaId_whenUpdateSubGoal_thenThrowNotFoundException() {
        // given
        when(subGoalCustomRepository.findByMemberIdAndSubGoalId(anyLong(), anyLong())).thenReturn(
            Optional.empty());
        UpdateSubGoalRequest request = new UpdateSubGoalRequest("change complete", false);

        // when, then
        assertThatThrownBy(() -> subGoalService.updateSubGoal(loginMember, 1l, request))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining(SubGoalErrorCode.NOT_FOUND_SUB_GOAL.getMessage())
            .extracting("errorCode")
            .isEqualTo(SubGoalErrorCode.NOT_FOUND_SUB_GOAL);
    }

}
