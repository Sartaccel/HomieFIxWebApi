package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.AdminLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminLoginRepository extends JpaRepository<AdminLogin, Long> {
    Optional<AdminLogin> findByUsername(String username);
}