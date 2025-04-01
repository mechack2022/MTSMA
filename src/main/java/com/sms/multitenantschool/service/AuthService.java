package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.dto.LoginDTO;
import com.sms.multitenantschool.model.dto.TenantSignUpDTO;

public interface AuthService {
    String login(LoginDTO loginDto);

    String createTenant(TenantSignUpDTO tenantSignUpDTO);

    String verifyEmail(String token);
}
