package com.product.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bill_items")
@Data
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    private Double price;

    private Integer quantity;

    @Column(name = "item_total")
    private Double itemTotal;
}
