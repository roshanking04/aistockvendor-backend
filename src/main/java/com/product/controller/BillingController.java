package com.product.controller;

import com.product.dto.BillRequest;
import com.product.entity.Bill;
import com.product.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
@CrossOrigin(origins = "http://localhost:3000")
public class BillingController {

    @Autowired
    private BillService billService;

    // ── POST /billing/generate-bill ──
    // Calculates totals, saves bill, reduces stock
    @PostMapping("/generate-bill")
    public ResponseEntity<?> generateBill(
            @RequestBody BillRequest request,
            Authentication auth
    ) {
        try {
            String createdBy = auth != null ? auth.getName() : "admin";
            Bill bill = billService.generateBill(request, createdBy);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Bill generation failed: " + e.getMessage());
        }
    }

    // ── GET /billing/all ──
    @GetMapping("/all")
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    // ── GET /billing/{id} ──
    @GetMapping("/{id}")
    public ResponseEntity<?> getBillById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(billService.getBillById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
