package com.product.service;

import com.product.entity.Product;
import com.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public Product save(Product p) {
        return repo.save(p);
    }

    public List<Product> getAll() {
        return repo.findByIsActiveTrue();
    }

    public Product getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }
    // ✅ Filter by category
    public List<Product> getByCategory(String category) {
        return repo.findByCategoryAndIsActiveTrue(category);
    }
    public List<Product> search(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    public void delete(Integer id) {
        // Soft delete - just mark inactive
        Product p = getById(id);
        p.setIsActive(false);
        repo.save(p);
    }

    public Long getTotalStockIn() {
        Long stock = repo.getTotalStockIn();
        return stock != null ? stock : 0L;
    }

    public List<Product> getLowStockProducts() {
        return repo.findLowStockProducts();
    }

    // Reduce stock when a bill is generated
    public void reduceStock(Integer productId, Integer qty) {
        Product p = getById(productId);
        int newStock = Math.max(0, p.getStockQuantity() - qty);
        p.setStockQuantity(newStock);
        repo.save(p);
    }

    public long getTotalProductCount() {
        return repo.countByIsActiveTrue();
    }
}
