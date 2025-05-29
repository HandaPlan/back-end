package com.org.candoit.domain.subgoal.entity;

import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.global.BaseTimeEntity;
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
public class SubGoal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subGoalId;

    private String subGoalTitle;

    private Boolean isStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_goal_id")
    private MainGoal mainGoal;
}
