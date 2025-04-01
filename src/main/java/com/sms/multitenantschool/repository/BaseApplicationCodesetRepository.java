package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.BaseApplicationCodeset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseApplicationCodesetRepository extends JpaRepository<BaseApplicationCodeset, Long> {
    Optional<BaseApplicationCodeset> findByCode(String code);
}