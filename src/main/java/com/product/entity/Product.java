package com.product.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vendor_id")
    private Integer vendorId = 2;

    @Column(nullable = false)
    private String name;

    @Column(name = "selling_price", nullable = false)
    private Double price;

    @Column(name = "cost_price")          // ✅ removed nullable = false
    private Double costPrice;

    private String description;
    private String image;

    // ✅ Category field — used by ProductList filter
    private String category;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 100;

    @Column(name = "low_stock_alert")
    private Integer lowStockAlert = 5;

    @Column(name = "is_active")
    private Boolean isActive = true;
}