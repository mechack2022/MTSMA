package com.sms.multitenantschool.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GuardianResponseDTO {
    private Long id;
    private UUID guardianUuid;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String relationship;
    private List<Long> studentIds;  // Just storing student IDs instead of full Student objects
}