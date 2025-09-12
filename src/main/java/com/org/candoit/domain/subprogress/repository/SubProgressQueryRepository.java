package com.org.candoit.domain.subprogress.repository;

import com.org.candoit.domain.subprogress.dto.CheckedSubProgressResponse;
import com.org.candoit.domain.subprogress.dto.DateSlotRow;
import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import java.time.LocalDate;
import java.util.List;

public interface SubProgressQueryRepository {

    List<SubProgressCalDto> aggregate(List<Long> subGoalIds, LocalDate start, LocalDate end);
    List<DateSlotRow> getCheckedSubProgressByDate(List<Long> subGoalIds, LocalDate start, LocalDate end);
}
