package com.product.config;

import com.product.entity.AdminUser;
import com.product.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AdminUserRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default admin if not exists
        if (!adminRepo.existsByUsername("admin")) {
            AdminUser admin = new AdminUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // BCrypt hashed
            admin.setRole("ADMIN");
            admin.setFullName("Store Admin");
            admin.setIsActive(true);
            adminRepo.save(admin);
            System.out.println("✅ Default admin created → username: admin | password: admin123");
        }
    }
}
