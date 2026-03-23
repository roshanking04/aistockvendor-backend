package com.product.controller;

import com.product.config.JwtUtil;
import com.product.dto.LoginRequest;
import com.product.dto.LoginResponse;
import com.product.entity.AdminUser;
import com.product.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired private AdminUserRepository adminRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AdminUser user = adminRepo.findByUsername(request.getUsername()).orElse(null);
        if (user == null || !user.getIsActive())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(null, null, null, null, "Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(null, null, null, null, "Invalid username or password"));
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRole(), user.getFullName(), "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminUser newUser) {
        if (adminRepo.existsByUsername(newUser.getUsername()))
            return ResponseEntity.badRequest().body("Username already exists");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setIsActive(true);
        AdminUser saved = adminRepo.save(newUser);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        String token    = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        AdminUser user = adminRepo.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}
