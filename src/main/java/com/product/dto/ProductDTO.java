package com.product.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDTO {
    private String name;
    private Double price;
    private Double costPrice;       // optional
    private String description;
    private Integer stockQuantity;  // ✅ for setting stock when adding
    private String category;        // ✅ new
    private MultipartFile image;
}