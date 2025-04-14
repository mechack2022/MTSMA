package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clazzes", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"class_name", "year_level", "academic_year", "section"}))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Clazz extends BaseEntity {
    @Column(name = "clazz_uuid", nullable = false, unique = true, updatable = false)
    private UUID classUuid;

    @Column(nullable = false, unique = true)
    private String className;

    @Column(nullable = false)
    private String yearLevel;

    @Column(nullable = false)
    private Integer studentCount;

    @OneToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "section")
    private String section;

    @ManyToOne
    @JoinColumn(name = "room_number", referencedColumnName = "roomNumber", nullable = false)
    private Room room;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL)
    private List<ClazzSubjectTeacher> subjectAssignments = new ArrayList<>();

}
