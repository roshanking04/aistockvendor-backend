package com.product.dto;

import lombok.Data;
import java.util.List;

// ── Bill Request ──
// Full request body sent from React when generating a bill
@Data
public class BillRequest {
    private String customerName;
    private String customerPhone;
    private Double discount;
    private List<ProductItemDTO> items;
}
