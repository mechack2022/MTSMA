package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.mapper.TeacherMapper;
import com.sms.multitenantschool.model.dto.TeacherRequestDTO;
import com.sms.multitenantschool.model.dto.TeacherResponseDTO;
import com.sms.multitenantschool.model.entity.Staff;
import com.sms.multitenantschool.model.entity.Teacher;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.timeTable.ClazzSubjectTeacher;
import com.sms.multitenantschool.model.entity.timeTable.Subject;
import com.sms.multitenantschool.repository.SubjectRepository;
import com.sms.multitenantschool.repository.TeacherRepository;
import com.sms.multitenantschool.service.TeacherService;
import com.sms.multitenantschool.service.TenantService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TenantService tenantService;
    private final StaffServiceImpl staffService;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherMapper teacherMapper;

    public TeacherServiceImpl(TenantService tenantService, StaffServiceImpl staffService,
                              TeacherRepository teacherRepository, TeacherMapper teacherMapper,
                              SubjectRepository subjectRepository) {
        this.tenantService = tenantService;
        this.staffService = staffService;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional
    public TeacherResponseDTO create(TeacherRequestDTO request) {
        log.info("Starting teacher creation: teacherId={}, staffEmail={}",
                request.getTeacherId(), request.getStaffEmail());

        if (request == null) {
            log.error("Teacher creation failed: Request is null");
            throw new IllegalArgumentException("Request cannot be null");
        }

        Tenant tenant = tenantService.getActiveTenant();
        if (tenant == null) {
            log.error("Teacher creation failed: Tenant not found");
            throw new ResourceNotFoundException("Tenant", "Tenant not found", null);
        }
        UUID tenantUuid = tenant.getTenantUuid();
        log.debug("Active tenant: tenantUuid={}", tenantUuid);

        log.debug("Retrieving staff for email={}", request.getStaffEmail());
        Staff staff = staffService.getByEmail(request.getStaffEmail());
        if (staff == null) {
            log.error("Teacher creation failed: Staff not found with email {}", request.getStaffEmail());
            throw new EntityNotFoundException("Staff not found with email: " + request.getStaffEmail());
        }
        log.debug("Found staff: staffUuid={}, email={}", staff.getStaffUuid(), staff.getEmail());
//      Validate staffUuid
        if (!staff.getStaffUuid().equals(request.getStaffUuid())) {
            log.error("Teacher creation failed: Staff UUID {} does not match staff record {}",
                    request.getStaffUuid(), staff.getStaffUuid());
            throw new BadRequestException("Teacher", "Staff UUID does not match staff record");
        }
        // Validate staffUuid
        if (!staff.getEmail().equals(request.getStaffEmail())) {
            log.error("Teacher creation failed: Staff email {} does not match provided mail {} ", request.getStaffEmail(), staff.getEmail());
            throw new BadRequestException("Teacher", "Staff email does not match provided mail ");
        }

        // Remove redundant email check since getByEmail ensures match
        String staffEmail = staff.getEmail();
        log.debug("Checking for duplicates: teacherId={}, staffEmail={}, tenantUuid={}",
                request.getTeacherId(), staffEmail, tenantUuid);
        if (teacherRepository.existsByTeacherIdAndTenantUuid(request.getTeacherId(), tenantUuid)) {
            log.error("Teacher creation failed: teacher ID {} already exists for tenant {}",
                    request.getTeacherId(), tenantUuid);
            throw new BadRequestException("Teacher", "Teacher ID '" + request.getTeacherId() + "' already exists for this tenant");
        }
        if (teacherRepository.existsByStaffEmailAndTenantUuid(staffEmail, tenantUuid)) {
            log.error("Teacher creation failed: Email {} already exists for tenant {}",
                    staffEmail, tenantUuid);
            throw new BadRequestException("Teacher", "Email '" + staffEmail + "' already exists for this tenant");
        }

        log.debug("Mapping request to Teacher entity");
        Teacher teacher = teacherMapper.toEntity(request);
        teacher.setTenantUuid(tenantUuid);
        teacher.setStaff(staff);

        Set<ClazzSubjectTeacher> teachingAssignments = new HashSet<>();
        if (request.getSubjectTaughtIds() != null && !request.getSubjectTaughtIds().isEmpty()) {
            log.debug("Validating subjects: subjectTaughtIds={}", request.getSubjectTaughtIds());
            Set<Subject> subjects = subjectRepository.findAllById(request.getSubjectTaughtIds())
                    .stream()
                    .filter(subject -> subject.getTenantUuid().equals(tenantUuid))
                    .collect(Collectors.toSet());
            log.debug("Found subjects: count={}", subjects.size());
            if (subjects.size() != request.getSubjectTaughtIds().size()) {
                log.error("Teacher creation failed: Invalid or cross-tenant subjects provided. Expected {}, found {}",
                        request.getSubjectTaughtIds().size(), subjects.size());
                throw new BadRequestException("Teacher", "One or more subjects are invalid or belong to a different tenant");
            }

            for (Subject subject : subjects) {
                ClazzSubjectTeacher assignment = ClazzSubjectTeacher.builder()
                        .tenantUuid(tenantUuid)
                        .subject(subject)
                        .teacher(teacher)
                        .build();
                teachingAssignments.add(assignment);
            }
            log.debug("Created {} teaching assignments", teachingAssignments.size());
        } else {
            log.debug("No subjects assigned to teacher");
        }

        // Set mutable list and rely on cascade to save assignments
        teacher.setClassAssignments(new ArrayList<>(teachingAssignments));
        log.debug("Assigned teaching assignments to teacher");

        log.debug("Saving teacher to database");
        Teacher savedTeacher = teacherRepository.save(teacher);
        log.info("Teacher created successfully: id={}, teacherId={}", savedTeacher.getId(), savedTeacher.getTeacherId());

        log.debug("Mapping saved teacher to response DTO");
        TeacherResponseDTO responseDTO = teacherMapper.toResponseDTO(savedTeacher);
        responseDTO.setSubjectTaughtIds(savedTeacher.getClassAssignments().stream()
                .map(assignment -> assignment.getSubject().getId())
                .collect(Collectors.toSet()));
        log.debug("Returning response with subjectTaughtIds={}", responseDTO.getSubjectTaughtIds());
        return responseDTO;
    }
}