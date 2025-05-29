package com.org.candoit.domain.dailyaction.repository;

import com.org.candoit.domain.dailyaction.entity.DailyAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyActionRepository extends JpaRepository<DailyAction, Long> {

}
