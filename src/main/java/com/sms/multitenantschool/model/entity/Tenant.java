package com.sms.multitenantschool.model.entity;

import com.sms.multitenantschool.converter.TenantSettingsConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "public")
public class Tenant extends BaseEntity {

    @Column(name = "tenant_uuid", nullable = false, unique = true, updatable = false)
    private UUID tenantUuid;

    @Column(name = "tenant_name", nullable = false, unique = true)
    private String tenantName;

    @Convert(converter = TenantSettingsConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tenant_settings", columnDefinition = "jsonb")
    private TenantSettings settings;

    // Getters and Setters
    public TenantSettings getSettings() {
        return settings;
    }

    public void setSettings(TenantSettings settings) {
        this.settings = settings;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public UUID getTenantUuid() {
        return tenantUuid;
    }

    public void setTenantUuid(UUID tenantUuid) {
        this.tenantUuid = tenantUuid;
    }

    @Override
    protected void prePersistCustom() {
        if (tenantUuid == null) {
            tenantUuid = UUID.randomUUID();
        }
    }
}