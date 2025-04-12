package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "subjects", schema = "public",
                uniqueConstraints = @UniqueConstraint(columnNames = {"subject_name", "year_level"}))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Subject extends BaseEntity {

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

    @ManyToMany(mappedBy = "subjectsTaught")
    private List<Teacher> teachers;

    @ManyToMany(mappedBy = "subjects")
    private List<Clazz> classes;

    @Column(name = "is_elective")
    private Boolean isElective = false;

}
