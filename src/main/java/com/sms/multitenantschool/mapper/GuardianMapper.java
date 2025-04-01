package com.sms.multitenantschool.mapper;

import com.sms.multitenantschool.model.dto.GuardianRequestDTO;
import com.sms.multitenantschool.model.dto.GuardianResponseDTO;
import com.sms.multitenantschool.model.entity.Guardian;
import com.sms.multitenantschool.model.entity.Student;

import java.util.List;
import java.util.stream.Collectors;

public class GuardianMapper {
    public static Guardian toEntity(GuardianRequestDTO dto) {
        Guardian guardian = new Guardian();
        guardian.setFullName(dto.getFullName());
        guardian.setPhoneNumber(dto.getPhoneNumber());
        guardian.setEmail(dto.getEmail());
        guardian.setRelationship(dto.getRelationship());
        return guardian;
    }

    // Convert Guardian entity to GuardianResponseDTO
    public static GuardianResponseDTO toResponseDTO(Guardian guardian) {
        GuardianResponseDTO dto = new GuardianResponseDTO();
        dto.setId(guardian.getId());
        dto.setGuardianUuid(guardian.getGuardianUuid());
        dto.setFullName(guardian.getFullName());
        dto.setPhoneNumber(guardian.getPhoneNumber());
        dto.setEmail(guardian.getEmail());
        dto.setRelationship(guardian.getRelationship());

        // Map student IDs if students exist
        if (guardian.getStudents() != null && !guardian.getStudents().isEmpty()) {
            List<Long> studentIds = guardian.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toList());
            dto.setStudentIds(studentIds);
        }

        return dto;
    }

    // Update existing Guardian entity from GuardianRequestDTO
    public static void updateEntityFromDTO(Guardian guardian, GuardianRequestDTO dto) {
        guardian.setFullName(dto.getFullName());
        guardian.setPhoneNumber(dto.getPhoneNumber());
        guardian.setEmail(dto.getEmail());
        guardian.setRelationship(dto.getRelationship());
    }

    // Convert List of Guardians to List of GuardianResponseDTOs
    public static List<GuardianResponseDTO> toResponseDTOList(List<Guardian> guardians) {
        return guardians.stream()
                .map(GuardianMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static GuardianRequestDTO toRequestDTO(Guardian guardian) {
        GuardianRequestDTO req = new GuardianRequestDTO();
        if (guardian != null) {
            guardian.setFullName(guardian.getFullName());
            guardian.setPhoneNumber(guardian.getPhoneNumber());
            guardian.setEmail(guardian.getEmail());
            guardian.setRelationship(guardian.getRelationship());
            return req;
        }
        return null;
    }

}