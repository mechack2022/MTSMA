package com.sms.multitenantschool.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sms.multitenantschool.model.DTO.LoginDTO;
import com.sms.multitenantschool.model.DTO.TenantSignUpDTO;
import com.sms.multitenantschool.model.entity.Role;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.User;
import com.sms.multitenantschool.repository.RoleRepository;
import com.sms.multitenantschool.repository.TenantRepository;
import com.sms.multitenantschool.repository.UserRepository;
import com.sms.multitenantschool.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        SecurityContextHolder.clearContext(); // Reset security context before each test
    }

    // --- Tests for login() ---
    @Test
    void testLogin_success() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("aaaa123", "aaaa123");
        User user = new User();
        user.setUsername("aaaa123");
        user.setVerified(true);
        Authentication authentication = mock(Authentication.class);
        when(userRepository.findByUsername("aaaa123")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token");

        // Act
        String token = authService.login(loginDTO);

        // Assert
        assertEquals("jwt-token", token);
        verify(userRepository).findByUsername("aaaa123");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    void testLogin_userNotFound() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("user1", "password123");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginDTO));
        verify(userRepository).findByUsername("aaaa123");
        verifyNoInteractions(authenticationManager, jwtTokenProvider);
    }

    @Test
    void testLogin_emailNotVerified() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("user1", "password123");
        User user = new User();
        user.setUsername("user1");
        user.setVerified(false); // Not verified
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.login(loginDTO),
                "Email not verified. Please verify your email before logging in.");
        verify(userRepository).findByUsername("user1");
        verifyNoInteractions(authenticationManager, jwtTokenProvider);
    }

    // --- Tests for createTenant() ---
    @Test
    void testCreateTenant_success() {
        // Arrange
        TenantSignUpDTO signUpDTO = new TenantSignUpDTO("tenant1", "admin", "user1",
                "admin@example.com", "password123", "password123");
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = authService.createTenant(signUpDTO);

        // Assert
        assertEquals("Tenant account created. Please verify your email.", result);
        verify(userRepository).existsByEmail("admin@example.com");
        verify(tenantRepository).save(any(Tenant.class));
        verify(roleRepository).findByName("ROLE_ADMIN");
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(eq("admin@example.com"), anyString());
    }

    @Test
    void testCreateTenant_passwordsDoNotMatch() {
        // Arrange
        TenantSignUpDTO signUpDTO = new TenantSignUpDTO("tenant1", "admin", "user1",
                "admin@example.com", "password123", "differentPassword");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.createTenant(signUpDTO),
                "Passwords do not match");
        verifyNoInteractions(userRepository, tenantRepository, roleRepository, emailService);
    }

    @Test
    void testCreateTenant_emailAlreadyInUse() {
        // Arrange
        TenantSignUpDTO signUpDTO = new TenantSignUpDTO("tenant1", "admin", "user1",
                "admin@example.com", "password123", "password123");
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.createTenant(signUpDTO),
                "Email already in use");
        verify(userRepository).existsByEmail("admin@example.com");
        verifyNoInteractions(tenantRepository, roleRepository, emailService);
    }

    @Test
    void testCreateTenant_roleNotFound() {
        // Arrange
        TenantSignUpDTO signUpDTO = new TenantSignUpDTO("tenant1", "admin", "user1",
                "admin@example.com", "password123", "password123");
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> authService.createTenant(signUpDTO),
                "User role not found in the database");
        verify(userRepository).existsByEmail("admin@example.com");
        verify(roleRepository).findByName("ROLE_ADMIN");
        verifyNoInteractions(emailService);
    }

    // --- Tests for verifyEmail() ---
    @Test
    void testVerifyEmail_success() {
        // Arrange
        String token = "verification-token";
        User user = new User();
        user.setVerificationToken(token);
        user.setVerified(false);
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = authService.verifyEmail(token);

        // Assert
        assertEquals("Email verified successfully!", result);
        assertTrue(user.isVerified());
        assertNull(user.getVerificationToken());
        verify(userRepository).findByVerificationToken(token);
        verify(userRepository).save(user);
    }

    @Test
    void testVerifyEmail_invalidToken() {
        // Arrange
        String token = "invalid-token";
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.verifyEmail(token),
                "Invalid or expired token");
        verify(userRepository).findByVerificationToken(token);
        verifyNoMoreInteractions(userRepository);
    }
}