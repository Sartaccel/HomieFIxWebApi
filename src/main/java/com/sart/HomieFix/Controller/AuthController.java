package com.sart.HomieFix.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.sart.HomieFix.Entity.AdminLogin;
import com.sart.HomieFix.Utility.JwtUtil;
import com.sart.HomieFix.Service.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminLoginService adminLoginService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AdminLogin adminLogin) {
        if (adminLoginService.findByUsername(adminLogin.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        System.out.println("Password Before Hashing: " + adminLogin.getPassword());

        // Hash the password
        String encryptedPassword = passwordEncoder.encode(adminLogin.getPassword());
        System.out.println("Password After Hashing: " + encryptedPassword);

        // Set the hashed password
        adminLogin.setPassword(encryptedPassword);

        // Save the user
        adminLoginService.saveUser(adminLogin);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return adminLoginService.findByUsername(username)
                .map(adminLogin -> {
                    System.out.println("Entered Password: " + password);
                    System.out.println("Stored Hashed Password: " + adminLogin.getPassword());

                    // Use matches() to compare entered password with the stored hash
                    boolean passwordMatches = passwordEncoder.matches(password, adminLogin.getPassword());
                    System.out.println("Password Match Result: " + passwordMatches);

                    if (passwordMatches) {
                        String token = jwtUtil.generateToken(username);
                        Map<String, String> response = new HashMap<>();
                        response.put("token", token);
                        return ResponseEntity.ok(response);
                    } else {
                        System.out.println("Password does not match!");
                        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
                    }
                })
                .orElseGet(() -> {
                    System.out.println("User not found!");
                    return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
                });
    }
}