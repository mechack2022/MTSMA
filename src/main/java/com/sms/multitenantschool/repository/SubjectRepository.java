package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.timeTable.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository  extends JpaRepository<Subject, Long> {
   boolean existsBySubjectNameAndYearLevel(String subjectName, String yearLevel);
   boolean existsBySubjectCode(String subjectCode);
}
