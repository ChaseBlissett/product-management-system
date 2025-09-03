package com.learning.product_management_system;

import com.learning.product_management_system.Model.Product;

public class HelperTestMethods {

    public static Product initiateTestProduct() {
        Product product = new Product();
        product.setName("test");
        product.setDescription("test1");
        product.setQuantity(100);
        product.setPrice(10);
        return product;
    }

}
