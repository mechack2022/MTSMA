package com.sms.multitenantschool.model.entity;

import com.sms.multitenantschool.converter.TenantSettingsConverter;
import org.hibernate.annotations.JdbcTypeCode;
import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "public")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_uuid", nullable = false, unique = true, updatable = false)
    private UUID tenantUuid;

    @Column(name = "tenant_name", nullable = false, unique = true)
    private String tenantName;

    @Convert(converter = TenantSettingsConverter.class) // Use the custom converter
    @JdbcTypeCode(SqlTypes.JSON) // Explicitly specify JSONB type
    @Column(name = "tenant_settings", columnDefinition = "jsonb")
    private TenantSettings settings;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TenantSettings getSettings() {
        return settings;
    }

    public void setSettings(TenantSettings settings) {
        this.settings = settings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    @PrePersist
    protected void onCreate() {
        if (tenantUuid == null) {
            tenantUuid = UUID.randomUUID();
        }
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
