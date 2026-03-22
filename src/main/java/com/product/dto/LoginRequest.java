package com.product.dto;

import lombok.Data;
import java.util.List;

// ── Login Request ──
// Used by: POST /api/auth/login
@Data
public class LoginRequest {
    private String username;
    private String password;
}
