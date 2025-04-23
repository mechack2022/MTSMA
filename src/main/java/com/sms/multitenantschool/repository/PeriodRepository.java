package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.timeTable.Period;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepository extends JpaRepository<Period, Long> {

}
