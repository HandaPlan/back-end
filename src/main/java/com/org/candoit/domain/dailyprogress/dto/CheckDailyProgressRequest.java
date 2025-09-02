package com.org.candoit.domain.dailyprogress.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckDailyProgressRequest {
    private Boolean isChecked;
    private LocalDate checkedDate;
}
