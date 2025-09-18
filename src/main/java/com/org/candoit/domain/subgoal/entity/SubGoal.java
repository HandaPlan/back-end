package com.org.candoit.domain.subgoal.entity;

import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.global.BaseTimeEntity;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SubGoal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subGoalId;

    @Column(nullable = false)
    private String subGoalName;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isStore = false;

    @Column(nullable = false)
    private Integer slotNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_goal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MainGoal mainGoal;

    public void changeSubGoalProperty(String name, Boolean attainment){
        if(name != null && !name.isEmpty()) this.subGoalName = name;
        if(attainment != null) this.isStore = attainment;
    }
}