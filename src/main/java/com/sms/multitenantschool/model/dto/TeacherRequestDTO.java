package com.sms.multitenantschool.model.dto;

import com.sms.multitenantschool.model.entity.timeTable.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherRequestDTO {
    // UUID of the related Staff (required
    @NotNull(message = "Staff UUID is required")
    private UUID staffUuid;

    @NotNull(message = "Tenant UUID is required")
    private UUID tenantUuid;

    @NotBlank(message="teacher Id is requied")
    private String teacherId;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "Class teacher status is required")
    private Boolean isClassTeacher;

    @NotNull(message = "Staff email is required")
    private String staffEmail;
    // New field for subjects taught (IDs)
    private Set<Long> subjectTaughtIds;
}
