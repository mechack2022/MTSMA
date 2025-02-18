package com.sms.multitenantschool.model.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "public")
@Getter
@Setter
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_uuid", nullable = false, unique = true, updatable = false)
    private UUID tenantUuid;

    @Column(name = "tenant_name", nullable = false, unique = true)
    private String tenantName;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
