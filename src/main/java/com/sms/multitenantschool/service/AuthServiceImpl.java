package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.DTO.LoginDTO;
import com.sms.multitenantschool.model.DTO.TenantSignUpDTO;
import com.sms.multitenantschool.model.entity.Role;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.User;
import com.sms.multitenantschool.repository.RoleRepository;
import com.sms.multitenantschool.repository.TenantRepository;
import com.sms.multitenantschool.repository.UserRepository;
import com.sms.multitenantschool.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider, UserRepository userRoleRepository,
                   TenantRepository tenantRepository, EmailService emailService, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRoleRepository;
        this.tenantRepository = tenantRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
    }

    @Override
    public String login(LoginDTO loginDto) {
        // find user by username
        Optional<User> userOptional = userRepository.findByUsername(loginDto.getUsername());
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        // Check if user is verified
        if (!user.isVerified()) {
            throw new IllegalArgumentException("Email not verified. Please verify your email before logging in.");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    @Override
    public String createTenant(TenantSignUpDTO tenantSignUpDTO) {
        if (!tenantSignUpDTO.getPassword().equals(tenantSignUpDTO.getConfirmedPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        // Check if email already exists
        if (userRepository.existsByEmail(tenantSignUpDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        // Generate a new UUID for the tenant
        UUID tenantUuid = UUID.randomUUID();
        // Create Tenant entity
        Tenant tenant = new Tenant();
        tenant.setTenantName(tenantSignUpDTO.getTenantName());
        tenant.setTenantUuid(tenantUuid);
        tenant.setCreatedAt(LocalDateTime.now());
        tenantRepository.save(tenant);

        Optional<Role> userRole = roleRepository.findByName("ROLE_ADMIN");
        if (userRole.isEmpty()) {
            throw new IllegalStateException("User role not found in the database");
        }
        // Create User entity (Admin)
        User user = new User();
        user.setUserUuid(UUID.randomUUID());
        user.setName(tenantSignUpDTO.getAdminName());
        user.setUsername(tenantSignUpDTO.getUsername());
        user.setEmail(tenantSignUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(tenantSignUpDTO.getPassword()));
        user.setTenantUuid(tenantUuid);
        user.setVerified(false);
        user.setRoles(Collections.singleton(userRole.get()));
        user.setCreatedAt(LocalDateTime.now());
        // Generate verification token and store it
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
//        send email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        return "Tenant account created. Please verify your email.";
    }


    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return "Email verified successfully!";
    }

}
