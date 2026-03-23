package com.product.controller;

import com.product.service.BillService;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired private BillService billService;
    @Autowired private ProductService productService;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(defaultValue = "daily") String range) {
        Map<String, Object> stats = new HashMap<>();
        switch (range) {
            case "monthly" -> {
                stats.put("todayProfit", billService.getMonthlyRevenue());
                stats.put("todayLoss",   calculateLoss(billService.getMonthlyRevenue()));
            }
            case "yearly" -> {
                stats.put("todayProfit", billService.getYearlyRevenue());
                stats.put("todayLoss",   calculateLoss(billService.getYearlyRevenue()));
            }
            default -> {
                stats.put("todayProfit", billService.getTodayRevenue());
                stats.put("todayLoss",   calculateLoss(billService.getTodayRevenue()));
                stats.put("billCount",   billService.getTodayBillCount());
            }
        }
        stats.put("stockIn",       productService.getTotalStockIn());
        stats.put("stockOut",      billService.getMonthlyStockOut());
        stats.put("totalProducts", productService.getTotalProductCount());
        stats.put("lowStock",      productService.getLowStockProducts().size());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/chart/revenue")
    public ResponseEntity<?> getRevenueChart() {
        List<Object[]> raw = billService.getLast6MonthsRevenue();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : raw) {
            Map<String, Object> point = new HashMap<>();
            point.put("month",   row[0]);
            point.put("revenue", row[1]);
            point.put("gst",     row[2]);
            result.add(point);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockAlert() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }

    private Double calculateLoss(Double revenue) {
        if (revenue == null || revenue == 0) return 0.0;
        return Math.round(revenue * 0.04 * 100.0) / 100.0;
    }
}
