package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.mapper.SubjectMapper;
import com.sms.multitenantschool.mapper.SubjectMapperImpl;
import com.sms.multitenantschool.model.dto.SubjectRequestDTO;
import com.sms.multitenantschool.model.dto.SubjectResponseDTO;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.timeTable.Subject;
import com.sms.multitenantschool.repository.SubjectRepository;
import com.sms.multitenantschool.service.SubjectService;
import com.sms.multitenantschool.service.TenantService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final TenantService tenantService;
    private final SubjectMapperImpl subjectMapperImpl;

    @Transactional
    public List<SubjectResponseDTO> createSubjects(List<SubjectRequestDTO> requestDTOs) {
        if (requestDTOs == null || requestDTOs.isEmpty()) {
            throw new BadRequestException("Subject", "Request list is null or empty");
        }
        Tenant tenant = tenantService.getActiveTenant();
        if (tenant == null) {
            throw new ResourceNotFoundException("Tenant", "Tenant not found", null);
        }
        // Validate and map DTOs to entities
        List<Subject> subjects = requestDTOs.stream()
                .map(requestDTO -> {
                    // Check for unique constraints
                    if (subjectRepository.existsBySubjectNameAndYearLevel(
                            requestDTO.getSubjectName(), requestDTO.getYearLevel())) {
                        throw new BadRequestException("Subject",
                                "Subject with name " + requestDTO.getSubjectName() +
                                        " and year level " + requestDTO.getYearLevel() + " already exists");
                    }
                    if (subjectRepository.existsBySubjectCode(requestDTO.getSubjectCode())) {
                        throw new BadRequestException("Subject",
                                "Subject code " + requestDTO.getSubjectCode() + " already exists");
                    }
                    // Map DTO to entity
                    Subject subject = subjectMapper.toEntity(requestDTO);
                    // Set tenantUuid from the Tenant
                    subject.setTenantUuid(tenant.getTenantUuid());
                    subject.setArchived(0);
                    return subject;
                })
                .collect(Collectors.toList());
        // Save all subjects
        List<Subject> savedSubjects = subjectRepository.saveAll(subjects);
        // Map saved entities to ResponseDTOs
        return savedSubjects.stream()
                .map(subjectMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
