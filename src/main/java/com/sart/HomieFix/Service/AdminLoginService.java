package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.AdminLogin;
import com.sart.HomieFix.Repository.AdminLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AdminLoginService {

    @Autowired
    private AdminLoginRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AdminLogin saveUser(AdminLogin user) {
        return userRepository.save(user);
    }

    public Optional<AdminLogin> findByUsername(String username) {
        return userRepository.findByUsername(username);  // Fixed: Now using the instance
    }
}
