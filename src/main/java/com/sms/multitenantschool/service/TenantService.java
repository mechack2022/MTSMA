package com.sms.multitenantschool.service;

import com.sms.multitenantschool.Utils.SecurityUtils.SecurityUtil;
import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.TenantSettings;
import com.sms.multitenantschool.model.entity.User;
import com.sms.multitenantschool.repository.TenantRepository;
import com.sms.multitenantschool.security.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantService implements Serializable {

    private final TenantRepository tenantRepository;
    private final CustomUserDetailsService userService;

    public TenantService(TenantRepository tenantRepository, CustomUserDetailsService userService) {
        this.tenantRepository = tenantRepository;
        this.userService = userService;
    }

    public TenantSettings addTenantSetting(TenantSettings tenantSettings, String tenantUuid) {
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


    @Transactional
    public TenantSettings updateTenantSettings(Long tenantId, TenantSettings newSettings) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", tenantId));
        if (tenant.getArchived() == 1) {
            throw new IllegalStateException("Cannot update archived tenant");
        }
        // Update the settings
        tenant.setSettings(newSettings);
        Tenant updatedTenant = tenantRepository.save(tenant);
        return updatedTenant.getSettings();
    }


    @Transactional
    public void archiveTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", tenantId));
        tenant.setArchived(1);
        tenantRepository.save(tenant);
    }

    public Tenant getTenant(Long tenantId) {
        if (tenantId == null) {
            throw new IllegalArgumentException("tenant Id is required");
        }
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", tenantId));
    }

    public Tenant getTenantUuid(UUID tenantUuid) {
        if (tenantUuid == null) {
            throw new IllegalArgumentException("tenant Id is required");
        }
        return tenantRepository.findByTenantUuid(tenantUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", tenantUuid));
    }

    public Tenant getActiveTenant() {
        Optional<String> currentUser = SecurityUtil.getCurrentLoginTenant();
        if (currentUser.isEmpty()) {
            throw new BadRequestException("Login is Required", "User");
        }
        String username = currentUser.get();
        //use the currentLoginTenant to get the ADMIN
        User admin = userService.getUserByUsername(username);
        return getTenantUuid(admin.getTenantUuid());
    }
}
