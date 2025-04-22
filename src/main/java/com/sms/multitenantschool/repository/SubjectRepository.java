package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.timeTable.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository  extends JpaRepository<Subject, Long> {
   boolean existsBySubjectNameAndYearLevelAndTenantUuid(String subjectName, String yearLevel, UUID tenantUuid);
   boolean existsBySubjectCodeAndTenantUuid(String subjectCode, UUID tenantUuid);
}
