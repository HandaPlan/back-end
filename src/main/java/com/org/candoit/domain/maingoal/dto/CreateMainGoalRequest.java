package com.org.candoit.domain.maingoal.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMainGoalRequest {
    private String mainGoalName;
    private ArrayList<String> subGoalName;
}
