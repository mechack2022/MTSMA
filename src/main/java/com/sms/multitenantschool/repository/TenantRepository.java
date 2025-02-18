package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
