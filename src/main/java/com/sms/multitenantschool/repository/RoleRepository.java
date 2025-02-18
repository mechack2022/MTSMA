package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String roleAdmin);
}
