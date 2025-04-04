package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

    Optional<Guardian> findByEmail(String email);
}
