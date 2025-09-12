package com.org.candoit.domain.subprogress.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckedSubProgressResponse {
    LocalDate checkedDate;
    List<Integer> slotNum;
}
