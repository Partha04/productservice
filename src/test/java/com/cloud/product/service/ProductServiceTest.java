package com.cloud.product.service;

import com.cloud.product.dto.ProductResponse;
import com.cloud.product.repository.ProductRepository;
import com.cloud.product.testData.ProductTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.cloud.product.testData.ProductTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Nested
    class createProductTests {
        @Test
        void shouldSaveTheNewUserToDatabase() {
            ProductResponse productResponse = productService.saveNewProduct(PRODUCT_REQUEST);
            assertNotNull(productResponse.getId());
            assertEquals(PRODUCT_RESPONSE.getProductId(), productResponse.getProductId());
            assertEquals(PRODUCT_RESPONSE.getProductName(), productResponse.getProductName());
            assertEquals(PRODUCT_RESPONSE.getTags(), productResponse.getTags());
            assertEquals(PRODUCT_RESPONSE.getCustomFields(), productResponse.getCustomFields());
        }
        @Test
        void shouldSaveAnotherNewUserToDatabase() {
            ProductResponse productResponse = productService.saveNewProduct(PRODUCT_REQUEST1);
            assertNotNull(productResponse.getId());
            assertEquals(PRODUCT_RESPONSE1.getProductId(), productResponse.getProductId());
            assertEquals(PRODUCT_RESPONSE1.getProductName(), productResponse.getProductName());
            assertEquals(PRODUCT_RESPONSE1.getTags(), productResponse.getTags());
            assertEquals(PRODUCT_RESPONSE1.getCustomFields(), productResponse.getCustomFields());
        }

    }

}