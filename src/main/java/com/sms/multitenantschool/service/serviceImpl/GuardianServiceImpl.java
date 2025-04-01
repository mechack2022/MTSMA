package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.Utils.CommonUtils;
import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.model.dto.GuardianRequestDTO;
import com.sms.multitenantschool.model.entity.Guardian;
import com.sms.multitenantschool.repository.GuardianRepository;
import com.sms.multitenantschool.service.GuardianService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuardianServiceImpl implements GuardianService {

    private final GuardianRepository guardianRepository;

    public GuardianServiceImpl(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public Guardian save(GuardianRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new IllegalArgumentException("request is null");
        }
        Optional<Guardian> guardian = getGuardianByEmail(requestDTO.getEmail());
        if (guardian.isPresent()) {
            throw new BadRequestException(requestDTO.getEmail(), "This email has been taken");
        }
        Guardian newGuardian = Guardian.builder()
                .guardianUuid(CommonUtils.generateNewUuid())
                .email(requestDTO.getEmail())
                .fullName(requestDTO.getFullName())
                .relationship(requestDTO.getRelationship())
                .phoneNumber(requestDTO.getPhoneNumber())
                .build();
        guardianRepository.save(newGuardian);
        return newGuardian;
    }

    public Guardian getGuardian(Long guardianId) {
        if (guardianId == null) {
            throw new IllegalArgumentException("guardian Id is required");
        }
        return guardianRepository.findById(guardianId)
                .orElseThrow(() -> new ResourceNotFoundException("Guardian", "id", guardianId));
    }


    @Override
    public Optional<Guardian> getGuardianByEmail(String email) {
        return guardianRepository.findByEmail(email); // Ensure this returns Optional<Guardian>
    }


}
