package com.org.candoit.domain.dailyprogress.repository;

import com.org.candoit.domain.dailyprogress.entity.DailyProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {

}
