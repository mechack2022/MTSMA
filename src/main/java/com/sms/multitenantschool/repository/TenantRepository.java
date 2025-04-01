package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantUuid(UUID parsedTenantUuid);
    Optional<Tenant> findById(Long tenantId);

}
