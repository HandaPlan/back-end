package com.org.candoit.init;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.repository.DailyActionRepository;
import com.org.candoit.domain.dailyprogress.entity.DailyProgress;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressRepository;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.org.candoit.domain.maingoal.repository.MainGoalRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.member.entity.MemberRole;
import com.org.candoit.domain.member.entity.MemberStatus;
import com.org.candoit.domain.member.repository.MemberRepository;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.domain.subgoal.repository.SubGoalRepository;
import com.org.candoit.domain.subprogress.entity.SubProgress;
import com.org.candoit.domain.subprogress.repository.SubProgressRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockDataLoader implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final MainGoalRepository mainGoalRepository;
    private final DailyActionRepository dailyActionRepository;
    private final DailyProgressRepository dailyProgressRepository;
    private final SubGoalRepository subGoalRepository;
    private final SubProgressRepository subProgressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("목 데이터 삽입 시작");
        // Member
        Member mockMember1 = Member.builder()
            .email("test@email.com")
            .memberRole(MemberRole.ROLE_USER)
            .memberStatus(MemberStatus.ACTIVITY)
            .comment("한줄 소개입니다.")
            .password(passwordEncoder.encode("testtest"))
            .nickname("zzuharchive")
            .profilePath("profile.jpg")
            .build();

        memberRepository.save(mockMember1);

        // MainGoal
        MainGoal mockMainGoal1 = MainGoal.builder()
            .member(mockMember1)
            .lastAchievementRate(27)
            .thisAchievementRate(37)
            .mainGoalName("다이어트 하기")
            .mainGoalStatus(MainGoalStatus.ACTIVITY)
            .isRepresentative(Boolean.TRUE)
            .build();

        mainGoalRepository.save(mockMainGoal1);

        // SubGoal
        SubGoal mockSubGoal1 = SubGoal.builder()
            .mainGoal(mockMainGoal1)
            .subGoalTitle("유산소 하기")
            .mainGoal(mockMainGoal1)
            .isStore(Boolean.FALSE)
            .build();

        SubGoal mockSubGoal2 = SubGoal.builder()
            .mainGoal(mockMainGoal1)
            .subGoalTitle("식단하기")
            .mainGoal(mockMainGoal1)
            .isStore(Boolean.FALSE)
            .build();

        SubGoal mockSubGoal3 = SubGoal.builder()
            .mainGoal(mockMainGoal1)
            .subGoalTitle("근력운동하기")
            .mainGoal(mockMainGoal1)
            .isStore(Boolean.FALSE)
            .build();

        subGoalRepository.save(mockSubGoal1);
        subGoalRepository.save(mockSubGoal2);
        subGoalRepository.save(mockSubGoal3);

        // DailyAction
        DailyAction mockDailyAction1 = DailyAction.builder()
            .subGoal(mockSubGoal1)
            .dailyActionTitle("러닝머신 달리기")
            .content("인터벌로 달리기 최소 30분")
            .targetNum(4)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction2 = DailyAction.builder()
            .subGoal(mockSubGoal1)
            .dailyActionTitle("걷기")
            .content("피크민하면서 산책하기")
            .targetNum(3)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction3 = DailyAction.builder()
            .subGoal(mockSubGoal2)
            .dailyActionTitle("물 마시기")
            .content("물 1L 마시기")
            .targetNum(7)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction4 = DailyAction.builder()
            .subGoal(mockSubGoal2)
            .dailyActionTitle("영양제 먹기")
            .content("유산균, 비타민C, 콜라겐")
            .targetNum(7)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction5 = DailyAction.builder()
            .subGoal(mockSubGoal2)
            .dailyActionTitle("단백질 챙겨먹기")
            .content("단백질 40g 챙기기!")
            .targetNum(7)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction6 = DailyAction.builder()
            .subGoal(mockSubGoal3)
            .dailyActionTitle("등, 어깨, 삼두")
            .content("상체")
            .targetNum(2)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction7 = DailyAction.builder()
            .subGoal(mockSubGoal3)
            .dailyActionTitle("이두, 가슴")
            .content("상체")
            .targetNum(1)
            .isStore(Boolean.FALSE)
            .build();

        DailyAction mockDailyAction8 = DailyAction.builder()
            .subGoal(mockSubGoal3)
            .dailyActionTitle("하체")
            .content("하체")
            .targetNum(3)
            .isStore(Boolean.FALSE)
            .build();

        dailyActionRepository.save(mockDailyAction1);
        dailyActionRepository.save(mockDailyAction2);
        dailyActionRepository.save(mockDailyAction3);
        dailyActionRepository.save(mockDailyAction4);
        dailyActionRepository.save(mockDailyAction5);
        dailyActionRepository.save(mockDailyAction6);
        dailyActionRepository.save(mockDailyAction7);
        dailyActionRepository.save(mockDailyAction8);


        // SubProgress
        SubProgress mockSubProgress1 = SubProgress.builder()
            .subGoal(mockSubGoal2)
            .checkedDate(LocalDate.of(2025,5,3))
            .targetCount(21)
            .checkedCount(1)
            .build();
        SubProgress mockSubProgress2 = SubProgress.builder()
            .subGoal(mockSubGoal2)
            .checkedDate(LocalDate.of(2025,5,3))
            .targetCount(21)
            .checkedCount(2)
            .build();
        SubProgress mockSubProgress3 = SubProgress.builder()
            .subGoal(mockSubGoal3)
            .checkedDate(LocalDate.of(2025,5,3))
            .targetCount(1)
            .checkedCount(6)
            .build();


        subProgressRepository.save(mockSubProgress1);
        subProgressRepository.save(mockSubProgress2);
        subProgressRepository.save(mockSubProgress3);

        // DailyProgress
        DailyProgress mockDailyProgress1 = DailyProgress.builder()
            .dailyAction(mockDailyAction3)
            .checkedDate(LocalDate.of(2025,5,1))
            .isChecked(Boolean.TRUE)
            .build();

        DailyProgress mockDailyProgress2 = DailyProgress.builder()
            .dailyAction(mockDailyAction4)
            .checkedDate(LocalDate.of(2025,5,3))
            .isChecked(Boolean.TRUE)
            .build();

        DailyProgress mockDailyProgress3 = DailyProgress.builder()
            .dailyAction(mockDailyAction6)
            .checkedDate(LocalDate.of(2025,5,5))
            .isChecked(Boolean.TRUE)
            .build();

        dailyProgressRepository.save(mockDailyProgress1);
        dailyProgressRepository.save(mockDailyProgress2);
        dailyProgressRepository.save(mockDailyProgress3);


        log.info("목 데이터 삽입 종료");
    }
}
