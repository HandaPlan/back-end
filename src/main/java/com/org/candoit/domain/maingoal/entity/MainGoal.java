package com.org.candoit.domain.maingoal.entity;

import com.org.candoit.domain.member.entity.Member;
import com.org.candoit.global.BaseTimeEntity;
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
public class MainGoal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainGoalId;

    @Column(nullable = false)
    private String mainGoalName;

    @Column(nullable = false)
    private Boolean isRepresentative;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MainGoalStatus mainGoalStatus = MainGoalStatus.ACTIVITY;

    private Integer lastAchievementRate;

    private Integer thisAchievementRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public void uncheckRepresentation(){
        this.isRepresentative = Boolean.FALSE;
    }

    public void checkRepresentation(){
        this.isRepresentative = Boolean.TRUE;
    }

    public void updateMainGoal(String mainGoalName, MainGoalStatus mainGoalStatus){
        this.mainGoalName = mainGoalName;
        this.mainGoalStatus = mainGoalStatus;
    }
}