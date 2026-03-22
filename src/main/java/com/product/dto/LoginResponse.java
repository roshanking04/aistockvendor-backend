package com.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// ── Login Response ──
// Returned after successful login
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String fullName;
    private String message;
}
