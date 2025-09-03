package com.learning.product_management_system.ServiceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learning.product_management_system.HelperTestMethods;
import com.learning.product_management_system.ExceptionHandling.ResourceNotFoundException;
import com.learning.product_management_system.Model.Product;
import com.learning.product_management_system.Repository.ProductRepository;
import com.learning.product_management_system.Service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void shouldGetProductsSuccessfully() {
        // Arrange
        Product product1 = HelperTestMethods.initiateTestProduct();
        Product product2 = HelperTestMethods.initiateTestProduct();
        Product product3 = HelperTestMethods.initiateTestProduct();
        List<Product> expected = List.of(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2, product3));

        // Act
        List<Product> actual = productService.getProducts();

        // Assert
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetProductByIdSuccessfully() {
        // Arrange
        Product product = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product actual = productService.getProductById(productId);

        // Assert
        assertEquals(product, actual);
    }

    @Test
    void getProductByIdShouldThrowWhenIdDoesNotExist() {
        Long invalidId = 1L;
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(invalidId));

    }

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        Product product = HelperTestMethods.initiateTestProduct();
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product actual = productService.createProduct(product);

        // Assert
        assertEquals(product, actual);
    }

    @Test
    void createProductShouldThrowWhenNameExists() {
        Product product = HelperTestMethods.initiateTestProduct();
        when(productRepository.findByName(product.getName())).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> productService.createProduct(product));
    }

    @Test
    void shouldUpdateProductByIdSuccessfully() {
        // Arragne
        Product existingProduct = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        existingProduct.setId(productId);
        Product updatedProduct = HelperTestMethods.initiateTestProduct();
        updatedProduct.setName("newTest");
        updatedProduct.setDescription("newTest1");
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Product actual = productService.updateProductById(productId, updatedProduct);

        // Assert
        assertEquals(existingProduct, actual);
    }

    @Test
    void updateProductByIdShouldThrowWhenNameExists() {
        Product product = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        String invalidName = "NewTest";
        Product updatedProduct = HelperTestMethods.initiateTestProduct();
        updatedProduct.setName(invalidName);
        updatedProduct.setDescription("newTest1");
        Product conflictingProduct = updatedProduct;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.findByName(invalidName)).thenReturn(Optional.of(conflictingProduct));

        assertThrows(RuntimeException.class, () -> productService.updateProductById(productId, updatedProduct));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProductByIdShouldThrowWhenIdDoesNotExist() {
        Product product = HelperTestMethods.initiateTestProduct();
        Long invalidId = 1L;

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProductById(invalidId, product));

    }

    @Test
    void shouldDeleteProductByIdSuccessfully() {
        // Arrange
        Product product = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        product.setId(productId);
        Map<String, String> expected = Map.of(product.getName(), "deleted successfully");
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Map<String, String> actual = productService.deleteProductById(productId);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void deleteProductByIdShouldThrowWhenIdDoesNotExist() {
        Long invalidId = 1L;
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProductById(invalidId));
        verify(productRepository, never()).save(any(Product.class));
    }

}
