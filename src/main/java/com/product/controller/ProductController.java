package com.product.controller;

import com.product.dto.ProductDTO;
import com.product.entity.Product;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService service;

    private final String uploadDir =
        System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    // ── POST /product/add ──
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@ModelAttribute ProductDTO dto) throws Exception {

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Save image
        String fileName = null;
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            fileName = System.currentTimeMillis() + "_" + dto.getImage().getOriginalFilename();
            dto.getImage().transferTo(new File(uploadDir + fileName));
        }

        Product p = new Product();
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setCostPrice(dto.getCostPrice() != null ? dto.getCostPrice() : 0.0); // ✅ optional, default 0
        p.setDescription(dto.getDescription());
        p.setImage(fileName);
        p.setCategory(dto.getCategory());                              // ✅
        p.setStockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 100); // ✅ use provided qty
        p.setIsActive(true);

        return ResponseEntity.ok(service.save(p));
    }

    // ── GET /product/all ──
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ── GET /product/{id} ──
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── PUT /product/update/{id} ──
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @ModelAttribute ProductDTO dto
    ) throws Exception {

        Product p = service.getById(id);
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());

        if (dto.getCostPrice() != null)    p.setCostPrice(dto.getCostPrice());
        if (dto.getDescription() != null)  p.setDescription(dto.getDescription());
        if (dto.getStockQuantity() != null) p.setStockQuantity(dto.getStockQuantity());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + dto.getImage().getOriginalFilename();
            dto.getImage().transferTo(new File(uploadDir + fileName));
            p.setImage(fileName);
        }

        return ResponseEntity.ok(service.save(p));
    }

    // ── DELETE /product/delete/{id} ──
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    // ── GET /product/search?name=xxx ──
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.search(name));
    }

    // ── GET /product/low-stock ──
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStock() {
        return ResponseEntity.ok(service.getLowStockProducts());
    }
}