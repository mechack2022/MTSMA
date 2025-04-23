package com.sms.multitenantschool.model.entity.timeTable;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;
@Setter
@Getter
public class PeriodDTO {

    private UUID periodUuid;
    @NotNull(message = "period Unique Id is  required")
    private String periodId;
    @NotNull(message = "period start time required")
    private LocalTime startTime;
    @NotNull(message = "period end time required")
    private LocalTime endTime;
    @NotNull(message = "period day of the week required required")
    private Integer dayOfWeek;
}
