package com.org.candoit.domain.dailyprogress.service;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.dailyaction.exception.DailyActionErrorCode;
import com.org.candoit.domain.dailyaction.repository.DailyActionCustomRepository;
import com.org.candoit.domain.dailyprogress.dto.CheckDailyProgressRequest;
import com.org.candoit.domain.dailyprogress.entity.DailyProgress;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressCustomRepository;
import com.org.candoit.domain.dailyprogress.repository.DailyProgressRepository;
import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyProgressService {

    private final DailyProgressRepository dailyProgressRepository;
    private final DailyProgressCustomRepository dailyProgressCustomRepository;
    private final DailyActionCustomRepository dailyActionCustomRepository;

    public Boolean checkedDate(Member loginMember, Long dailyActionId,
        CheckDailyProgressRequest checkDailyProgressRequest) {

        DailyAction da = dailyActionCustomRepository.findByMemberIdAndDailyActionId(
            loginMember.getMemberId(), dailyActionId).orElseThrow(() -> new CustomException(
            DailyActionErrorCode.NOT_FOUND_DAILY_ACTION));

        // false -> 삭제
        if (checkDailyProgressRequest.getIsChecked() == Boolean.FALSE) {
            dailyProgressCustomRepository.deleteByDailyActionIdAndCheckedDate(da.getDailyActionId(),
                checkDailyProgressRequest.getCheckedDate());
        }
        // true -> 저장
        else {
            dailyProgressRepository.save(DailyProgress.builder()
                .dailyAction(da)
                .checkedDate(checkDailyProgressRequest.getCheckedDate())
                .build());
        }
        return Boolean.TRUE;
    }
}
