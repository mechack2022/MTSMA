package com.sms.multitenantschool.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TenantSignUpDTO {
    private String tenantName;
    private String adminName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmedPassword;
}
