package com.sms.multitenantschool.security;

import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.model.entity.User;
import com.sms.multitenantschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                authorities
        );
    }

    public User getUserByTenantUuid(UUID tenantUuid) {
        if (tenantUuid == null) {
            throw new BadRequestException("User", "Email is required");
        }
        return userRepository.findByTenantUuid(tenantUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email Not Found", tenantUuid));
    }

    public User getUserByEmail(String email) {
        if (email == null) {
            throw new BadRequestException("User", "Email is required");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email Not Found", email));
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            throw new BadRequestException("User", "Email is required");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username Not Found", username
                ));
    }

}