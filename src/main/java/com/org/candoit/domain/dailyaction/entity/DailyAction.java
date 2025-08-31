package com.org.candoit.domain.dailyaction.entity;

import com.org.candoit.domain.subgoal.entity.SubGoal;
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
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DailyAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyActionId;

    @Column(nullable = false)
    private String dailyActionTitle;

    private String content;

    @Column(nullable = false)
    private Integer targetNum;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isStore = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_goal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SubGoal subGoal;

    public void updateDailyAction(String title, String content, Integer targetNum, Boolean attainment) {
        this.dailyActionTitle = title;
        this.content = content;
        this.targetNum = targetNum;
        this.isStore = attainment;
    }
}