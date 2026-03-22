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
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    @Autowired
    private AdminUserRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ── POST /api/auth/login ──
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // 1. Find user by username
        AdminUser user = adminRepo.findByUsername(request.getUsername())
                .orElse(null);

        // 2. Check user exists and is active
        if (user == null || !user.getIsActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, null, "Invalid username or password"));
        }

        // 3. Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, null, "Invalid username or password"));
        }

        // 4. Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getUsername(),
                user.getRole(),
                user.getFullName(),
                "Login successful"
        ));
    }

    // ── POST /api/auth/register (create new admin user) ──
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminUser newUser) {
        if (adminRepo.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setIsActive(true);
        AdminUser saved = adminRepo.save(newUser);
        saved.setPassword(null); // don't return password
        return ResponseEntity.ok(saved);
    }

    // ── GET /api/auth/me (get logged-in user info from token) ──
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        String token    = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        String role     = jwtUtil.extractRole(token);

        AdminUser user = adminRepo.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        user.setPassword(null); // never return password
        return ResponseEntity.ok(user);
    }
}
