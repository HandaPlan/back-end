package com.org.candoit.domain.subgoal.dto;

import com.org.candoit.domain.dailyaction.dto.CreateDailyActionRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateSubGoalRequest {

    @NotBlank
    private String name;
    @NotNull @Min(1) @Max(8)
    private Integer slotNum;
    private List<CreateDailyActionRequest> dailyActions;
}
