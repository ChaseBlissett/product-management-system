package com.learning.product_management_system.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learning.product_management_system.Model.Product;
import com.learning.product_management_system.Service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    
    ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping()
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public Product updateProductById(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProductById(id, product);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProductById(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

}
