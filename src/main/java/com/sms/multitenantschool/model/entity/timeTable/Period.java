package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "periods", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Period extends BaseEntity {

    @Column(name = "period_uuid", nullable = false, unique = true, updatable = false)
    private UUID periodUuid;

    @Column(name = "period_id", nullable = false, unique = true, updatable = false)
    private String periodId;

    @Column(nullable= false, name= "start_time")
    private LocalTime startTime;

    @Column(nullable= false, name= "end_time")
    private LocalTime endTime;

}
