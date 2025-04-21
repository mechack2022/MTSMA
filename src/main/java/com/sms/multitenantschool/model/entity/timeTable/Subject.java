package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subjects", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subject_name", "year_level"}))
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Subject extends BaseEntity {

    @Column(name = "tenant_uuid", nullable = false)
    private UUID tenantUuid;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String yearLevel;

    @Column(name = "subject_code", nullable = false, unique = true)
    private String subjectCode;

    @Column(name = "description")
    private String description;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "is_elective")
    private Boolean isElective = false;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<ClazzSubjectTeacher> classAssignments = new ArrayList<>();
}


