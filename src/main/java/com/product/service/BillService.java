package com.product.service;

import com.product.dto.BillRequest;
import com.product.dto.ProductItemDTO;
import com.product.entity.Bill;
import com.product.entity.BillItem;
import com.product.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private ProductService productService;

    private static final double GST_RATE = 0.18;

    // ── Calculate & Save Bill ──
    public Bill generateBill(BillRequest request, String createdBy) {

        double subTotal = 0;

        // Build bill items & calculate subtotal
        List<BillItem> billItems = new ArrayList<>();
        for (ProductItemDTO item : request.getItems()) {
            double itemTotal = item.getPrice() * item.getQuantity();
            subTotal += itemTotal;

            BillItem bi = new BillItem();
            bi.setProductId(item.getId());
            bi.setProductName(item.getName());
            bi.setPrice(item.getPrice());
            bi.setQuantity(item.getQuantity());
            bi.setItemTotal(itemTotal);
            billItems.add(bi);

            // ✅ Reduce stock for each product sold
            if (item.getId() != null) {
                productService.reduceStock(item.getId(), item.getQuantity());
            }
        }

        double discount    = request.getDiscount() != null ? request.getDiscount() : 0;
        double taxableAmt  = subTotal - discount;
        double gstAmount   = taxableAmt * GST_RATE;
        double grandTotal  = taxableAmt + gstAmount;

        // Build bill entity
        Bill bill = new Bill();
        bill.setCustomerName(request.getCustomerName());
        bill.setCustomerPhone(request.getCustomerPhone());
        bill.setSubTotal(subTotal);
        bill.setDiscount(discount);
        bill.setGstAmount(Math.round(gstAmount * 100.0) / 100.0);
        bill.setGrandTotal(Math.round(grandTotal * 100.0) / 100.0);
        bill.setItemsCount(request.getItems().size());
        bill.setCreatedAt(LocalDateTime.now());
        bill.setCreatedBy(createdBy);
        bill.setItems(billItems);

        return billRepo.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepo.findAllByOrderByCreatedAtDesc();
    }

    public Bill getBillById(Integer id) {
        return billRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + id));
    }

    public Double getTodayRevenue() {
        Double rev = billRepo.getTodayRevenue();
        return rev != null ? rev : 0.0;
    }

    public Integer getTodayBillCount() {
        Integer count = billRepo.getTodayBillCount();
        return count != null ? count : 0;
    }

    public Double getMonthlyRevenue() {
        Double rev = billRepo.getMonthlyRevenue();
        return rev != null ? rev : 0.0;
    }

    public Double getYearlyRevenue() {
        Double rev = billRepo.getYearlyRevenue();
        return rev != null ? rev : 0.0;
    }

    public Integer getMonthlyStockOut() {
        Integer out = billRepo.getMonthlyStockOut();
        return out != null ? out : 0;
    }

    public List<Object[]> getLast6MonthsRevenue() {
        return billRepo.getLast6MonthsRevenue();
    }
}
