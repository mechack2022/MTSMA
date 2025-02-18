package com.sms.multitenantschool.controller;

import com.sms.multitenantschool.model.AuthResponseDTO;
import com.sms.multitenantschool.model.DTO.LoginDTO;
import com.sms.multitenantschool.model.DTO.TenantSignUpDTO;
import com.sms.multitenantschool.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDto) {

        String token = authService.login(loginDto);

        AuthResponseDTO authResponseDto = new AuthResponseDTO();
        authResponseDto.setAccessToken(token);

        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @PostMapping("/tenant-signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody TenantSignUpDTO tenantSignUpDTO) {

        String token = authService.createTenant(tenantSignUpDTO);

        AuthResponseDTO authResponseDto = new AuthResponseDTO();
        authResponseDto.setAccessToken(token);

        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

}