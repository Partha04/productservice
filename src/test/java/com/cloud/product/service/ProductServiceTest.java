package com.cloud.product.service;

import com.cloud.product.dto.ProductResponse;
import com.cloud.product.repository.ProductRepository;
import com.cloud.product.util.MongoContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static com.cloud.product.testData.ProductTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest extends MongoContainer {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Nested
    class createProductTests {
        @Test
        void shouldSaveTheNewUserToDatabase() {
            ProductResponse productResponse = productService.saveNewProduct(PRODUCT_REQUEST);
            checkSavedProduct(productResponse, PRODUCT_RESPONSE);
        }

        @Test
        void shouldSaveAnotherNewUserToDatabase() {
            ProductResponse productResponse = productService.saveNewProduct(PRODUCT_REQUEST1);
            checkSavedProduct(productResponse, PRODUCT_RESPONSE1);
        }

        private void checkSavedProduct(ProductResponse productResponse, ProductResponse productResponse1) {
            assertNotNull(productResponse.getId());
            assertEquals(productResponse1.getProductId(), productResponse.getProductId());
            assertEquals(productResponse1.getProductName(), productResponse.getProductName());
            assertEquals(productResponse1.getTags(), productResponse.getTags());
            assertEquals(productResponse1.getCustomFields(), productResponse.getCustomFields());
            assertTrue(productRepository.existsById(productResponse.getProductId()));
        }
    }

    @Nested
    class getProducts {
        @BeforeEach
        void setUp() {
            productRepository.deleteAll();
            productRepository.save(PRODUCT);
            productRepository.save(PRODUCT1);
        }

        @Test
        void shouldGiveSingleSavedProductsFromFirstPage() {
            Page<ProductResponse> product = productService.getProduct(FIRST_PAGE_WITH_SIZE_ONE);
            assertEquals(1, product.getSize());
            assertEquals(PRODUCT.getProductId(), product.getContent().get(0).getProductId());
            assertEquals(PRODUCT.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT.getCustomFields(), product.getContent().get(0).getCustomFields());
        }

        @Test
        void shouldGiveTheSavedProductFromPage1Size2() {
            Page<ProductResponse> product = productService.getProduct(FIRST_PAGE_WITH_SIZE_TWO);
            assertEquals(2, product.getSize());
            assertEquals(PRODUCT.getProductId(), product.getContent().get(0).getProductId());
            assertEquals(PRODUCT.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT.getCustomFields(), product.getContent().get(0).getCustomFields());

            assertEquals(PRODUCT1.getProductId(), product.getContent().get(1).getProductId());
            assertEquals(PRODUCT1.getProductName(), product.getContent().get(1).getProductName());
            assertEquals(PRODUCT1.getPrice(), product.getContent().get(1).getPrice());
            assertEquals(PRODUCT1.getTags(), product.getContent().get(1).getTags());
            assertEquals(PRODUCT1.getCustomFields(), product.getContent().get(1).getCustomFields());
        }

        @Test
        void shouldGiveTheSavedProductFromPage1Size2SortedByName() {
            Page<ProductResponse> product = productService.getProduct(FIRST_PAGE_WITH_SIZE_TWO);
            assertEquals(2, product.getSize());
            assertEquals(PRODUCT.getProductId(), product.getContent().get(0).getProductId());
            assertEquals(PRODUCT.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT.getCustomFields(), product.getContent().get(0).getCustomFields());

            assertEquals(PRODUCT1.getProductId(), product.getContent().get(1).getProductId());
            assertEquals(PRODUCT1.getProductName(), product.getContent().get(1).getProductName());
            assertEquals(PRODUCT1.getPrice(), product.getContent().get(1).getPrice());
            assertEquals(PRODUCT1.getTags(), product.getContent().get(1).getTags());
            assertEquals(PRODUCT1.getCustomFields(), product.getContent().get(1).getCustomFields());
        }

        @Test
        void shouldGiveSavedUserFromPage2Size1() {
            Page<ProductResponse> product = productService.getProduct(SECOND_PAGE_WITH_SIZE_ONE);
            assertEquals(1, product.getSize());
            assertEquals(PRODUCT1.getProductId(), product.getContent().get(0).getProductId());
            assertEquals(PRODUCT1.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT1.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT1.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT1.getCustomFields(), product.getContent().get(0).getCustomFields());
        }
    }


}