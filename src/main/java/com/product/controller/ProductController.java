package com.product.controller;

import com.product.dto.ProductDTO;
import com.product.entity.Product;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    // ── Use absolute path so uploads work on Railway ──
  /*   private String getUploadDir() {
        String dir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        File folder = new File(dir);
        if (!folder.exists()) folder.mkdirs();
        return dir;
    }
*/private String getUploadDir() {
    // On Railway, 'user.dir' is the root of your app
    String dir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    File folder = new File(dir);
    if (!folder.exists()) {
        folder.mkdirs(); // Line to ensure folder exists
    }
    return dir;
}
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@ModelAttribute ProductDTO dto) throws Exception {
        String uploadDir = getUploadDir();
        String fileName = null;
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            fileName = System.currentTimeMillis() + "_" + dto.getImage().getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            dto.getImage().transferTo(path.toFile());
        }
        Product p = new Product();
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setCostPrice(dto.getCostPrice() != null ? dto.getCostPrice() : 0.0);
        p.setDescription(dto.getDescription());
        p.setImage(fileName);
        p.setCategory(dto.getCategory());
        p.setStockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 100);
        p.setIsActive(true);
        return ResponseEntity.ok(service.save(p));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try { return ResponseEntity.ok(service.getById(id)); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @ModelAttribute ProductDTO dto) throws Exception {
        Product p = service.getById(id);
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        if (dto.getCostPrice()     != null) p.setCostPrice(dto.getCostPrice());
        if (dto.getDescription()   != null) p.setDescription(dto.getDescription());
        if (dto.getStockQuantity() != null) p.setStockQuantity(dto.getStockQuantity());
        if (dto.getCategory()      != null) p.setCategory(dto.getCategory());
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String uploadDir = getUploadDir();
            String fileName  = System.currentTimeMillis() + "_" + dto.getImage().getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            dto.getImage().transferTo(path.toFile());
            p.setImage(fileName);
        }
        return ResponseEntity.ok(service.save(p));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStock() {
        return ResponseEntity.ok(service.getLowStockProducts());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }
}
