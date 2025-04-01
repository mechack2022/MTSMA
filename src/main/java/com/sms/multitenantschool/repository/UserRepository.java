package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByTenantUuid(UUID tenantUuid);
    Optional<User> findByEmail(String email);
}
