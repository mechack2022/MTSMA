package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {
  boolean  existsByTeacherIdAndTenantUuid(String teacherId, UUID tenantUuid);
   boolean  existsByStaffEmailAndTenantUuid(String email, UUID tenantUuid);
}
