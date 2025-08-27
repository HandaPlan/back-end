package com.org.candoit.maingoal.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.maingoal.service.MainGoalService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import com.org.candoit.domain.subprogress.repository.SubProgressQueryRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;
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
class MainGoalServiceTest {

    @Mock MainGoalRepository mainGoalRepository;
    @Mock MainGoalCustomRepository mainGoalCustomRepository;
    @Mock SubGoalRepository subGoalRepository;
    @Mock DailyActionRepository dailyActionRepository;
    @Mock SubGoalCustomRepository subGoalCustomRepository;
    @Mock SubProgressQueryRepository subProgressQueryRepository;

    @Mock Clock clock;
    @InjectMocks MainGoalService mainGoalService;

    Member member;

    @BeforeEach
    void setup() {
        member = Member.builder()
            .memberId(1L)
            .nickname("테스트용 사용자")
            .build();
    }

    @DisplayName("메인골 상세 페이지 조회 성공")
    @Test
    void givenExistsMainGoalId_whenReadMainGoalDetails_thenReturnMainGoalDetails() {
        // given
        MainGoal savedMainGoal = MainGoal.builder()
            .mainGoalStatus(MainGoalStatus.ACTIVITY)
            .mainGoalName("test용 메인골")
            .member(member)
            .build();

        List<SubGoal> subGoals = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            subGoals.add(SubGoal.builder()
                .subGoalId(i + 1)
                .subGoalName("서브골 " + (i + 1))
                .mainGoal(savedMainGoal)
                .slotNum((int) i + 1)
                .isStore(false)
                .build());
        }

        when(mainGoalCustomRepository.findByMainGoalIdAndMemberId(anyLong(), anyLong()))
            .thenReturn(Optional.of(savedMainGoal));
        when(subGoalCustomRepository.findByMainGoalId(anyLong()))
            .thenReturn(subGoals);

        when(clock.getZone()).thenReturn(ZoneId.of("Asia/Seoul"));
        when(clock.instant()).thenReturn(Instant.parse("2025-08-20T10:00:00Z"));

        List<SubProgressCalDto> agg = List.of(
            new SubProgressCalDto(101L, 5, 2),
            new SubProgressCalDto(102L, 5, 5)
        );
        subGoals.forEach(sg -> {
            when(subProgressQueryRepository.aggregate(eq(sg.getSubGoalId()), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(agg);
        });

        // when
        var res = mainGoalService.getMainGoalDetails(member, /*mainGoalId*/ 999L);

        // then
        assertNotNull(res);
        assertEquals("test용 메인골", res.getMainGoal().getName());
        assertEquals(MainGoalStatus.ACTIVITY, res.getMainGoal().getStatus());

        assertEquals(5, res.getSubGoals().size());
        assertEquals("서브골 1", res.getSubGoals().get(0).getName());
        assertEquals(1, res.getSubGoals().get(0).getSlotNum());

        assertEquals(LocalDate.of(2025, 8, 18), res.getProgress().getStartDate());
        assertEquals(LocalDate.of(2025, 8, 24), res.getProgress().getEndDate());

        assertEquals(5, res.getProgress().getSubProgress().size());
        assertEquals(70, res.getProgress().getSubProgress().get(0).getRate());
    }
}
