package com.org.candoit.domain.dailyaction.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class ActionDates {

    private Long dailyActionId;
    private List<LocalDate> dates;
}
