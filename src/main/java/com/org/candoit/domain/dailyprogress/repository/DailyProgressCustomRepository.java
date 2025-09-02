package com.org.candoit.domain.dailyprogress.repository;

import java.time.LocalDate;
import java.util.List;

public interface DailyProgressCustomRepository {

    List<LocalDate> distinctCheckedDate(Long subGoalId, LocalDate start, LocalDate end);
    void deleteByDailyActionIdAndCheckedDate(Long dailyActionId, LocalDate checkedDate);
}
