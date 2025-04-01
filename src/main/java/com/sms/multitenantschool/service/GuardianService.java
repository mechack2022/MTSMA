package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.entity.Guardian;

import java.util.Optional;

public interface GuardianService {
    Optional<Guardian> getGuardianByEmail(String email);
}
