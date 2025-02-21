package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.TenantSettings;
import com.sms.multitenantschool.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantService implements Serializable {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository){
        this.tenantRepository = tenantRepository;
    }

    public TenantSettings addTenantSetting(TenantSettings tenantSettings, String tenantUuid) {
        // Validate the tenantUuid
        if (tenantUuid == null || tenantUuid.isEmpty()) {
            throw new IllegalArgumentException("Tenant UUID cannot be null or empty.");
        }
        // Parse the tenantUuid into a UUID object
        UUID parsedTenantUuid = UUID.fromString(tenantUuid);
        Optional<Tenant> optionalTenant = tenantRepository.findByTenantUuid(parsedTenantUuid);
        if (optionalTenant.isEmpty()) {
            throw new RuntimeException("Tenant not found with UUID: " + tenantUuid);
        }
        Tenant tenant = optionalTenant.get();
        tenant.setSettings(tenantSettings);
        Tenant updatedTenant = tenantRepository.save(tenant);
        return updatedTenant.getSettings();
    }
}
