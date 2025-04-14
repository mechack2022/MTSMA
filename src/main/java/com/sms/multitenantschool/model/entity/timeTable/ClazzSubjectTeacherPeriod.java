package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clazz_subject_teacher_period", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"clazz_subject_teacher_id", "period_id"}))
@NoArgsConstructor
@Getter
@Setter
public class ClazzSubjectTeacherPeriod extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "clazz_subject_teacher_id", nullable = false)
    private ClazzSubjectTeacher clazzSubjectTeacher;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    public ClazzSubjectTeacherPeriod(ClazzSubjectTeacher cst, Period period) {
        this.clazzSubjectTeacher = cst;
        this.period = period;
    }
}

