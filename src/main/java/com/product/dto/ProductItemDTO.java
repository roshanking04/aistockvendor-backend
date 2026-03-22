package com.product.dto;

import lombok.Data;

// ── Product Item DTO ──
// Used inside BillRequest for each cart item
@Data
public class ProductItemDTO {
    private Integer id;
    private String name;
    private Double price;
    private Double costPrice;   // ✅ REQUIRED
    private Integer quantity;
}
