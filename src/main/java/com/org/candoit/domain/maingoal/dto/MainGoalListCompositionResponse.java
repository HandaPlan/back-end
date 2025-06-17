package com.org.candoit.domain.maingoal.dto;

import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainGoalListCompositionResponse {

    private Long id;
    private String name;
    private MainGoalStatus status;
    private Boolean isRep;
}
