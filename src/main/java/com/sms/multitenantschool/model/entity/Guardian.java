package com.sms.multitenantschool.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "guardians", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Guardian extends BaseEntity {

    @Column(name = "guardian_uuid", nullable = false, unique = true, updatable = false)
    private UUID guardianUuid;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "relationship", nullable = false)
    private String relationship;

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();

    @Override
    protected void prePersistCustom() {
        if (guardianUuid == null) {
            guardianUuid = UUID.randomUUID();
        }
    }

}

