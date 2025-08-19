package com.org.candoit.domain.subgoal.entity;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.global.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    private String subGoalName;

    private Boolean isStore;

    @Column(nullable = false)
    private Integer slotNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_goal_id")
    private MainGoal mainGoal;

    @OneToMany(mappedBy = "subGoal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DailyAction> dailyActions = new ArrayList<>();
}
