package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
