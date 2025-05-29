package com.org.candoit.domain.dailyaction.entity;

import com.org.candoit.domain.subgoal.entity.SubGoal;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DailyAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyActionId;

    private String dailyActionTitle;

    private String content;

    private Integer targetNum;

    private Boolean isStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_goal_id")
    private SubGoal subGoal;
}
