package com.product.repository;

import com.product.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    // All bills newest first
    List<Bill> findAllByOrderByCreatedAtDesc();

    // Today's revenue
    @Query(value = "SELECT COALESCE(SUM(grand_total), 0) FROM bills WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    Double getTodayRevenue();

    // Today's bill count
    @Query(value = "SELECT COUNT(*) FROM bills WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    Integer getTodayBillCount();

    // Monthly revenue
    @Query(value = "SELECT COALESCE(SUM(grand_total), 0) FROM bills WHERE MONTH(created_at) = MONTH(CURDATE()) AND YEAR(created_at) = YEAR(CURDATE())", nativeQuery = true)
    Double getMonthlyRevenue();

    // Yearly revenue
    @Query(value = "SELECT COALESCE(SUM(grand_total), 0) FROM bills WHERE YEAR(created_at) = YEAR(CURDATE())", nativeQuery = true)
    Double getYearlyRevenue();

    // Monthly stock out (items sold)
    @Query(value = "SELECT COALESCE(SUM(items_count), 0) FROM bills WHERE MONTH(created_at) = MONTH(CURDATE())", nativeQuery = true)
    Integer getMonthlyStockOut();

    // Last 6 months revenue for chart
    @Query(value = """
        SELECT DATE_FORMAT(created_at, '%b') as month,
               SUM(grand_total) as revenue,
               SUM(gst_amount) as gst
        FROM bills
        WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
        GROUP BY DATE_FORMAT(created_at, '%Y-%m')
        ORDER BY MIN(created_at)
        """, nativeQuery = true)
    List<Object[]> getLast6MonthsRevenue();
}
