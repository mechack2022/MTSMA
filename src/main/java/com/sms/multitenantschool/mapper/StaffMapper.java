package com.sms.multitenantschool.mapper;

import com.sms.multitenantschool.model.dto.StaffRequestDTO;
import com.sms.multitenantschool.model.dto.StaffResponseDTO;
import com.sms.multitenantschool.model.entity.Staff;
import org.springframework.stereotype.Service;

@Service
public class StaffMapper {

    public StaffResponseDTO toResponseDto(Staff staff) {
        if (staff == null) {
            return null;
        }
        StaffResponseDTO dto = new StaffResponseDTO();
        dto.setId(staff.getId());
        dto.setStaffUuid(staff.getStaffUuid());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setEmail(staff.getEmail());
        dto.setPhoneNumber(staff.getPhoneNumber());
        dto.setJobTitle(staff.getJobTitle());
        dto.setTenantUuid(staff.getTenantUuid());
        dto.setCvFileName(staff.getCvFileName());
        dto.setCvContentType(staff.getCvContentType());
        dto.setCvFilePath(staff.getCvFilePath());
        dto.setImageFileName(staff.getImageFileName());
        dto.setImageContentType(staff.getImageContentType());
        dto.setImageFilePath(staff.getImageFilePath());
        dto.setArchived(staff.getArchived());
        return dto;
    }

    public Staff toEntity(StaffRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setJobTitle(dto.getJobTitle());
        staff.setArchived(0);
        return staff;
    }

    public void updateEntityFromRequestDto(Staff staff, StaffRequestDTO dto) {
        if (dto == null || staff == null) {
            return;
        }
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
//        staff.setEmail(dto.getEmail());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setJobTitle(dto.getJobTitle());
    }
}
