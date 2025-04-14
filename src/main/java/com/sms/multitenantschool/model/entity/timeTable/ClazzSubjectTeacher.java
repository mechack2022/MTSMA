package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clazz_subject_teacher", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"clazz_id", "subject_id", "teacher_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClazzSubjectTeacher extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "clazz_id", nullable = false)
    private Clazz clazz;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "clazzSubjectTeacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClazzSubjectTeacherPeriod> periodAssignments = new HashSet<>();

    public void addPeriod(Period period) {
        ClazzSubjectTeacherPeriod cstPeriod = new ClazzSubjectTeacherPeriod(this, period);
        periodAssignments.add(cstPeriod);
    }
}



