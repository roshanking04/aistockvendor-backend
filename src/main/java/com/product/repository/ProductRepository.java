package com.product.repository;

import com.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Search by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Only active products
    List<Product> findByIsActiveTrue();
    // ✅ Filter by category
    List<Product> findByCategoryAndIsActiveTrue(String category);

    // Low stock products
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.lowStockAlert AND p.isActive = true")
    List<Product> findLowStockProducts();

    // Total stock count
    @Query("SELECT SUM(p.stockQuantity) FROM Product p WHERE p.isActive = true")
    Long getTotalStockIn();

    // Total active product count
    long countByIsActiveTrue();
}
