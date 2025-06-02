package com.org.candoit.domain.maingoal.entity;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.domain.subgoal.entity.SubGoal;
import com.org.candoit.global.BaseTimeEntity;
import jakarta.persistence.CascadeType;
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
public class MainGoal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainGoalId;

    private String mainGoalName;

    private Boolean isRepresentative;

    @Enumerated(EnumType.STRING)
    private MainGoalStatus mainGoalStatus;

    private Integer lastAchievementRate;

    private Integer thisAchievementRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "mainGoal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SubGoal> subGoals = new ArrayList<>();

    public void uncheckRepresentation(){
        this.isRepresentative = Boolean.FALSE;
    }

    public void checkRepresentation(){
        this.isRepresentative = Boolean.TRUE;
    }
}
