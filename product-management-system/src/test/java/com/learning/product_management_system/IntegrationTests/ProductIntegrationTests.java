package com.learning.product_management_system.IntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import com.learning.product_management_system.HelperTestMethods;
import com.learning.product_management_system.Model.Product;
import com.learning.product_management_system.Service.ProductService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductIntegrationTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        ProductService productService;

        @Test
        public void shouldReturnAllProudcts() throws Exception {
                Product product1 = HelperTestMethods.initiateTestProduct();
                Product product2 = HelperTestMethods.initiateTestProduct();
                product2.setName("test2");
                Product product3 = HelperTestMethods.initiateTestProduct();
                product3.setName("test3");
                productService.createProduct(product1);
                productService.createProduct(product2);
                productService.createProduct(product3);

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

                productService.createProduct(product);

                mockMvc.perform(get("/api/v1/products/{id}", product.getId()))
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
                                .andExpect(jsonPath("$.price").value(1000))
                                .andExpect(jsonPath("$.inventory").value(100000));
        }

        @Test
        void shouldReturnUpdatedProduct() throws Exception {
                Product product = HelperTestMethods.initiateTestProduct();
                productService.createProduct(product);
                Product updatedProduct = HelperTestMethods.initiateTestProduct();
                updatedProduct.setName("newTest");
                updatedProduct.setDescription("newTest1");

                String productJson = """
                                {
                                "name" : "newTest",
                                "description" : "newTest1",
                                "quantity" : 100,
                                "price" : 10
                                }
                                """;

                mockMvc.perform(put("/api/v1/products/{id}", product.getId())
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
                productService.createProduct(product);

                mockMvc.perform(delete("/api/v1/products/{id}", product.getId()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.test").value("deleted successfully"));

        }
}