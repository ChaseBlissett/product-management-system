package com.learning.product_management_system.ControllerTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.learning.product_management_system.HelperTestMethods;
import com.learning.product_management_system.Model.Product;
import com.learning.product_management_system.Service.ProductService;
import com.learning.product_management_system.Controller.ProductController; 

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Test
    public void shouldReturnAllProudcts() throws Exception {
        Product product1 = HelperTestMethods.initiateTestProduct();
        Product product2 = HelperTestMethods.initiateTestProduct();
        Product product3 = HelperTestMethods.initiateTestProduct();
        when(productService.getProducts()).thenReturn(List.of(product1, product2, product3));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[0].description").value("test1"))
                .andExpect(jsonPath("$[0].quantity").value(100))
                .andExpect(jsonPath("$[0].price").value(10))
                .andExpect(jsonPath("$[0].inventory").value(1000));
    }

    @Test
    public void shouldReturnProductById() throws Exception {
        Product product = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        product.setId(productId);
        when(productService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("test1"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.inventory").value(1000));
    }

    @Test
    public void shouldReturnCreatedProduct() throws Exception {

        Product product = HelperTestMethods.initiateTestProduct();

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        String productJson = """
                {
                "name" : "test",
                "description" : "test1",
                "quantity" : 100,
                "price" : 1000
                }
                """;

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("test1"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.inventory").value(1000));
    }

    @Test
    void shouldReturnUpdatedProduct() throws Exception {
        Product product = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        product.setId(productId);
        Product updatedProduct = HelperTestMethods.initiateTestProduct();
        updatedProduct.setName("newTest");
        updatedProduct.setDescription("newTest1");
        when(productService.updateProductById(eq(productId), any(Product.class))).thenReturn(updatedProduct);

        String productJson = """
                {
                "name" : "newTest",
                "description" : "newTest1",
                "quantity" : 100,
                "price" : 10
                }
                """;

        mockMvc.perform(put("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("newTest"))
                .andExpect(jsonPath("$.description").value("newTest1"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    void shouldReturnDeletedProduct() throws Exception {
        Product product = HelperTestMethods.initiateTestProduct();
        Long productId = 1L;
        product.setId(productId);
        when(productService.deleteProductById(productId))
                        .thenReturn(Map.of(product.getName(), "deleted successfully"));

        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.test").value("deleted successfully"));

    }

}
