package com.org.candoit.domain.subprogress.repository;

import com.org.candoit.domain.subprogress.dto.SubProgressCalDto;
import java.time.LocalDate;
import java.util.List;

public interface SubProgressQueryRepository {

    List<SubProgressCalDto> aggregate(Long subGoalId, LocalDate start, LocalDate end);
}
