package com.org.candoit.maingoal.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.maingoal.dto.MainGoalDetailsResponse;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.repository.MainGoalCustomRepository;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.maingoal.service.MainGoalService;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.dto.DetailSubProgressResponse;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalCustomRepository;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.dto.Direction;
import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import com.org.candoit.domain.subprogress.dto.SubProgressOverviewResponse;
import com.org.candoit.domain.subprogress.repository.SubProgressQueryRepository;

import com.org.candoit.domain.subprogress.service.SubProgressService;
import com.org.candoit.global.util.DateTimeUtil;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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

    @Mock
    MainGoalRepository mainGoalRepository;
    @Mock
    MainGoalCustomRepository mainGoalCustomRepository;
    @Mock
    SubGoalRepository subGoalRepository;
    @Mock
    DailyActionRepository dailyActionRepository;
    @Mock
    SubGoalCustomRepository subGoalCustomRepository;
    @Mock
    SubProgressQueryRepository subProgressQueryRepository;
    @Mock
    SubProgressService subProgressService;

    @Mock
    Clock clock;
    @InjectMocks
    MainGoalService mainGoalService;

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
        for (int i = 0; i < 5; i++) {
            subGoals.add(SubGoal.builder()
                .subGoalId((long) (i + 1))
                .subGoalName("서브골 " + (i + 1))
                .mainGoal(savedMainGoal)
                .slotNum(i + 1)
                .isStore(false)
                .build());
        }

        when(mainGoalCustomRepository.findByMainGoalIdAndMemberId(anyLong(), anyLong()))
            .thenReturn(Optional.of(savedMainGoal));

        when(subGoalCustomRepository.findByMainGoalId(anyLong()))
            .thenReturn(subGoals);

        // 고정 시간
        when(clock.getZone()).thenReturn(ZoneId.of("Asia/Seoul"));
        when(clock.instant()).thenReturn(Instant.parse("2025-08-20T10:00:00Z"));
        LocalDate now = LocalDate.ofInstant(clock.instant(), clock.getZone());

        LocalDate expectedMonday = now.with(
            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 2025-08-18
        LocalDate expectedSunday = now.with(
            TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));     // 2025-08-24
        int expectedWeekOfMonth = DateTimeUtil.getWeekOfMonth(now);

        List<DetailSubProgressResponse> details = List.of(
            new DetailSubProgressResponse("서브골 1", 1, 70),
            new DetailSubProgressResponse("서브골 2", 2, 10),
            new DetailSubProgressResponse("서브골 3", 3, 20),
            new DetailSubProgressResponse("서브골 4", 4, 30),
            new DetailSubProgressResponse("서브골 5", 5, 40)
        );

        SubProgressOverviewResponse subProgressOverviewResponse = SubProgressOverviewResponse.builder()
            .startDate(expectedMonday)
            .endDate(expectedSunday)
            .weekOfMonth(expectedWeekOfMonth)
            .subProgress(details)
            .build();

        when(subProgressService.getProgress(anyList(), any(LocalDate.class), eq(Direction.CURRENT)))
            .thenReturn(subProgressOverviewResponse);

        MainGoalDetailsResponse result = mainGoalService.getMainGoalDetails(member, 1l);

        // then
        assertNotNull(result);
        assertEquals("test용 메인골", result.getMainGoal().getName());
        assertEquals(MainGoalStatus.ACTIVITY, result.getMainGoal().getStatus());

        assertEquals(5, result.getSubGoals().size());
        assertEquals("서브골 1", result.getSubGoals().get(0).getName());
        assertEquals(1, result.getSubGoals().get(0).getSlotNum());

        assertEquals(LocalDate.of(2025, 8, 18), result.getProgress().getStartDate());
        assertEquals(LocalDate.of(2025, 8, 24), result.getProgress().getEndDate());

        assertEquals(5, result.getProgress().getSubProgress().size());
        assertEquals(70, result.getProgress().getSubProgress().get(0).getRate());
    }

}
