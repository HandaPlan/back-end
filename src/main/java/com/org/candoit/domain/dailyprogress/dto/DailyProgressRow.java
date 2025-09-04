package com.org.candoit.domain.dailyprogress.dto;

import java.time.LocalDate;

public record DailyProgressRow(Long dailyActionId, LocalDate checkedDate){}