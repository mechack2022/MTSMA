package com.sms.multitenantschool.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequestDTO {

    @NotBlank(message = "Tenant Uuid is required")
    private UUID tenantUuid;

    @NotBlank(message = "Subject name is required")
    private String subjectName;

    @NotBlank(message = "Year level is required")
    private String yearLevel;

    @NotBlank(message = "Subject code is required")
    private String subjectCode;

    private String description;

    @NotNull(message = "Credits are required")
    @Positive(message = "Credits must be positive")
    private Integer credits;

    @NotNull(message = "Elective status is required")
    private Boolean isElective;
}
