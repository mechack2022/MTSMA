package com.sms.multitenantschool.repository;

import com.sms.multitenantschool.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);
}
