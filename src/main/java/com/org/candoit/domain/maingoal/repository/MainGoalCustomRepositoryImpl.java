package com.org.candoit.domain.maingoal.repository;

import static com.org.candoit.domain.maingoal.entity.QMainGoal.mainGoal;
import static com.org.candoit.domain.member.entity.QMember.member;

import com.org.candoit.domain.maingoal.entity.MainGoal;
import com.org.candoit.domain.maingoal.entity.MainGoalStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MainGoalCustomRepositoryImpl implements MainGoalCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<MainGoal> findByMainGoalIdAndMemberId(Long mainGoalId, Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.select(mainGoal)
            .from(mainGoal)
            .innerJoin(member)
            .on(mainGoal.member.memberId.eq(memberId))
            .where(mainGoal.mainGoalId.eq(mainGoalId)).fetchOne());
    }

    @Override
    public Optional<MainGoal> findActiveMainGoal(Long mainGoalId, Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.select(mainGoal)
            .from(mainGoal)
            .where(mainGoal.mainGoalStatus.in(MainGoalStatus.ATTAINMENT, MainGoalStatus.PAUSE))
            .innerJoin(member)
            .on(mainGoal.member.memberId.eq(memberId))
            .where(mainGoal.mainGoalId.eq(mainGoalId)).fetchOne());
    }

    @Override
    public Optional<MainGoal> findRepresentativeMainGoalByMemberId(Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.select(mainGoal)
            .from(mainGoal)
            .innerJoin(member)
            .on(mainGoal.member.memberId.eq(memberId))
            .where(
                (mainGoal.isRepresentative.eq(Boolean.TRUE)))
            .fetchOne());
    }

    @Override
    public List<MainGoal> findByMemberIdAndStatus(Long memberId, MainGoalStatus status) {
        return jpaQueryFactory.select(mainGoal)
            .from(mainGoal)
            .where((mainGoal.member.memberId.eq(memberId)).and(
                checkStatus(status))).fetch();
    }

    @Override
    public List<MainGoal> findByMemberId(Long memberId) {
        return jpaQueryFactory.select(mainGoal)
            .from(mainGoal)
            .where((mainGoal.member.memberId.eq(memberId)))
            .fetch();
    }

    @Override
    public Optional<MainGoal> findOldestByMemberId(Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.select(mainGoal)
            .from(mainGoal)
            .where(mainGoal.member.memberId.eq(memberId)
                .and(
                    mainGoal.mainGoalStatus.in(MainGoalStatus.ACTIVITY, MainGoalStatus.ATTAINMENT)))
            .orderBy(mainGoal.mainGoalId.asc())
            .fetchFirst());
    }

    @Override
    public Boolean existsActiveMainGoal(Long memberId) {
        Integer searchResult = jpaQueryFactory.selectOne()
            .from(mainGoal)
            .where(
                mainGoal.member.memberId.eq(memberId)
                    .and(
                    mainGoal.mainGoalStatus.in(MainGoalStatus.ACTIVITY, MainGoalStatus.ATTAINMENT)))
            .fetchFirst();
        return searchResult != null;
    }

    private BooleanExpression checkStatus(MainGoalStatus status) {
        if (status == null) {
            return null;
        }
        return mainGoal.mainGoalStatus.eq(status);
    }
}
