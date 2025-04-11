package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clazzes", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Clazz extends BaseEntity {
    @Column(name = "clazz_uuid", nullable = false, unique = true, updatable = false)
    private UUID classUuid;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String yearLevel;

    @Column(nullable = false)
    private Integer studentCount;

    @OneToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;

    @ManyToMany
    @JoinTable(
            name = "clazz_subjects",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Subject> subjects;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "section")
    private String section;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
