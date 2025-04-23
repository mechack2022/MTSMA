package com.sms.multitenantschool.model.entity.timeTable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "periods", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_periods_day_time_tenant",
                        columnNames = {"day_of_week", "start_time", "tenant_uuid"})
        })
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Period extends BaseEntity {

    @Column(name = "period_uuid", nullable = false)
    private UUID periodUuid;

    @Column(name = "period_id", nullable = false)
    private String periodId;

    @Column(name = "tenant_uuid", nullable = false)
    private UUID tenantUuid  ;

    @Column(nullable= false, name= "start_time")
    private LocalTime startTime;

    @Column(nullable= false, name= "end_time")
    private LocalTime endTime;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @OneToMany(mappedBy = "period")
    private List<ClazzSubjectTeacherPeriod> periodAssignments = new ArrayList<>();

}
