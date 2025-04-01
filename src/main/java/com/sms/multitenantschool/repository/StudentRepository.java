package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.dto.StudentResponseDTO;
import com.sms.multitenantschool.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {
   Optional<Student> findByEmail(String email);
   List<Student> findAllByTenantId(Long tenantId);
}
