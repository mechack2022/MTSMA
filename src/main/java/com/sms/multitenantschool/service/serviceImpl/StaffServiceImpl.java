package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.mapper.StaffMapper;
import com.sms.multitenantschool.model.DTO.StaffRequestDTO;
import com.sms.multitenantschool.model.DTO.StaffResponseDTO;
import com.sms.multitenantschool.model.entity.Staff;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.repository.StaffRepository;
import com.sms.multitenantschool.repository.TenantRepository;
import com.sms.multitenantschool.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StaffServiceImpl {
    private final StaffRepository staffRepository;
    private final TenantRepository tenantRepository;
    private final StaffMapper staffMapper;
    private final FileService fileService;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository, TenantRepository tenantRepository,
                            StaffMapper staffMapper, FileService fileService) {
        this.staffRepository = staffRepository;
        this.tenantRepository = tenantRepository;
        this.staffMapper = staffMapper;
        this.fileService = fileService;
    }

    @Transactional
    public StaffResponseDTO createStaff(Long tenantId, StaffRequestDTO staffRequestDto) throws IOException {
        if (tenantId == null || staffRequestDto == null) {
            throw new IllegalArgumentException("Tenant ID and staff details cannot be null");
        }
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", "id", tenantId));
        // Check if staff with the same email already exists
        Optional<Staff> existingStaff = staffRepository.findByEmail(staffRequestDto.getEmail());
        if (existingStaff.isPresent()) {
            throw new BadRequestException("email", "A staff member with this email already exists");
        }

        Staff staff = staffMapper.toEntity(staffRequestDto);
        staff.setTenantUuid(tenant.getTenantUuid());
        Staff savedStaff = staffRepository.save(staff);

        // Handle CV upload if provided
        if (staffRequestDto.getCvFile() != null && !staffRequestDto.getCvFile().isEmpty()) {
            String cvFilePath = fileService.uploadFile(savedStaff.getId().toString(), "staff", staffRequestDto.getCvFile());
            savedStaff.setCvFileName(staffRequestDto.getCvFile().getOriginalFilename());
            savedStaff.setCvContentType(staffRequestDto.getCvFile().getContentType());
            savedStaff.setCvFilePath(cvFilePath);
        }

        // Handle image upload if provided
        if (staffRequestDto.getImageFile() != null && !staffRequestDto.getImageFile().isEmpty()) {
            String imageFilePath = fileService.uploadFile(savedStaff.getId().toString(), "staff-images", staffRequestDto.getImageFile());
            savedStaff.setImageFileName(staffRequestDto.getImageFile().getOriginalFilename());
            savedStaff.setImageContentType(staffRequestDto.getImageFile().getContentType());
            savedStaff.setImageFilePath(imageFilePath);
        }

        savedStaff = staffRepository.save(savedStaff);
        return staffMapper.toResponseDto(savedStaff);
    }

    @Transactional
    public StaffResponseDTO uploadCv(Long staffId, MultipartFile cvFile) throws IOException {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
        if (staff.getArchived() == 1) {
            throw new IllegalStateException("Cannot modify archived staff");
        }

        String cvFilePath = fileService.uploadFile(staffId.toString(), "staff", cvFile);
        staff.setCvFileName(cvFile.getOriginalFilename());
        staff.setCvContentType(cvFile.getContentType());
        staff.setCvFilePath(cvFilePath);
        Staff updatedStaff = staffRepository.save(staff);
        return staffMapper.toResponseDto(updatedStaff);
    }

    @Transactional
    public StaffResponseDTO uploadImage(Long staffId, MultipartFile imageFile) throws IOException {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
        if (staff.getArchived() == 1) {
            throw new IllegalStateException("Cannot modify archived staff");
        }

        String imageFilePath = fileService.uploadFile(staffId.toString(), "staff-images", imageFile);
        staff.setImageFileName(imageFile.getOriginalFilename());
        staff.setImageContentType(imageFile.getContentType());
        staff.setImageFilePath(imageFilePath);
        Staff updatedStaff = staffRepository.save(staff);
        return staffMapper.toResponseDto(updatedStaff);
    }

    public Resource downloadCv(Long staffId) throws IOException {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
        if (staff.getCvFilePath() == null) {
            throw new ResourceNotFoundException("CV", "staffId", staffId);
        }
        return fileService.downloadFile(staff.getCvFilePath());
    }

    public Resource downloadImage(Long staffId) throws IOException {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
        if (staff.getImageFilePath() == null) {
            throw new ResourceNotFoundException("Image", "staffId", staffId);
        }
        return fileService.downloadFile(staff.getImageFilePath());
    }

    public StaffResponseDTO findById(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
        return staffMapper.toResponseDto(staff);
    }

    @Transactional
    public StaffResponseDTO updateStaff(Long staffId, StaffRequestDTO staffRequestDto) throws IOException {
        if (staffId == null || staffRequestDto == null) {
            throw new IllegalArgumentException("Staff ID and staff details cannot be null");
        }

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));

        if (staff.getArchived() == 1) {
            throw new IllegalStateException("Cannot modify archived staff");
        }

        //  Preserve the original email (DO NOT update it)
        String existingEmail = staff.getEmail();

        //  Update only allowed fields
        staffMapper.updateEntityFromRequestDto(staff, staffRequestDto);
        //  Restore the original email to prevent updates
        staff.setEmail(existingEmail);
        //  Handle CV upload if provided
        if (staffRequestDto.getCvFile() != null && !staffRequestDto.getCvFile().isEmpty()) {
            String cvFilePath = fileService.uploadFile(staffId.toString(), "staff", staffRequestDto.getCvFile());
            staff.setCvFileName(staffRequestDto.getCvFile().getOriginalFilename());
            staff.setCvContentType(staffRequestDto.getCvFile().getContentType());
            staff.setCvFilePath(cvFilePath);
        }

        // ✅ Handle image upload if provided
        if (staffRequestDto.getImageFile() != null && !staffRequestDto.getImageFile().isEmpty()) {
            String imageFilePath = fileService.uploadFile(staffId.toString(), "staff-images", staffRequestDto.getImageFile());
            staff.setImageFileName(staffRequestDto.getImageFile().getOriginalFilename());
            staff.setImageContentType(staffRequestDto.getImageFile().getContentType());
            staff.setImageFilePath(imageFilePath);
        }

        Staff updatedStaff = staffRepository.save(staff);
        return staffMapper.toResponseDto(updatedStaff);
    }

    @Transactional
    public String deleteStaff(Long staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID cannot be null");
        }
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));

        if (staff.getArchived() == 1) {
            throw new IllegalStateException("Staff is already archived");
        }
        staff.setArchived(1);
        staffRepository.save(staff);
        return "Staff archived successfully";
    }


}
