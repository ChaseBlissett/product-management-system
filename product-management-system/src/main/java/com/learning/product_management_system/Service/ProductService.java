package com.learning.product_management_system.Service;
import com.learning.product_management_system.ExceptionHandling.ResourceNotFoundException;
import com.learning.product_management_system.Model.Product;
import com.learning.product_management_system.Repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
    }

    public Product createProduct(Product product) {
        Optional<Product> potentialExistingProduct = productRepository.findByName(product.getName());
        if (potentialExistingProduct.isPresent()) {
            throw new RuntimeException("Product with this name already exists");
        }
        return productRepository.save(product);
    }

    public Product updateProductById(Long id, Product product) {
         Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
        
        // Check if another product with this title exists
        Optional<Product> potentialExistingProduct = productRepository.findByName(product.getName());
        if (potentialExistingProduct.isPresent() && !product.getName().equals(existingProduct.getName())) {
            throw new RuntimeException("Product with this name already exists");
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

    public Map<String, String> deleteProductById(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
            productRepository.delete(product);
            return Map.of(product.getName(), "deleted successfully");
        }
        catch(ResourceNotFoundException ex) {
            throw ex;
        }
        catch(RuntimeException ex) {
            throw new RuntimeException("Something went wrong. Unable to delete product.");
        }
    }

}
