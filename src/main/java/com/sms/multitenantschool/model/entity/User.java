package com.sms.multitenantschool.model.entity;//package com.sms.multitenantschool.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "user_uuid", nullable = false, unique = true, updatable = false)
    private UUID userUuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name= "tenant_uuid",nullable = false)
    private UUID tenantUuid;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "verification_token", unique = true)
    private String verificationToken;

    @Column(nullable = false)
    private boolean verified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    public void generateUUID() {
        if (userUuid == null) {
            userUuid = UUID.randomUUID();
        }
    }

    @PreUpdate
    public void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }
}

