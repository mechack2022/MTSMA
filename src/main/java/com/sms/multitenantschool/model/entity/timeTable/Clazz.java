package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "classes", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_classes_name_year_level_tenant",
                        columnNames = {"class_name", "year_level", "tenant_uuid"})
        })
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Clazz extends BaseEntity {
    @Column(name = "clazz_uuid", nullable = false, unique = true, updatable = false)
    private UUID classUuid;

    @Column(name = "tenant_uuid", nullable = false)
    private UUID tenantUuid;

    @Column(name = "class_name", nullable = false, unique = true)
    private String className;

    @Column(name="year_level", nullable = false)
    private String yearLevel;

    @Column(nullable = false)
    private Integer studentCount = 0;

    @OneToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "section")
    private String section;

//    @ManyToOne
//    @JoinColumn(name = "room_number", referencedColumnName = "roomNumber", nullable = false)
//    private Room room;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClazzSubjectTeacher> subjectAssignments = new ArrayList<>();


}
