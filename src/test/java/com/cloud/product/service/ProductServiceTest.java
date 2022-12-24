package com.cloud.product.service;

import com.cloud.product.dto.ProductResponse;
import com.cloud.product.exception.CustomException;
import com.cloud.product.model.Product;
import com.cloud.product.repository.ProductRepository;
import com.cloud.product.util.ErrorMessages;
import com.cloud.product.util.MongoContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.cloud.product.testData.ProductTestData.*;
import static com.cloud.product.util.ErrorMessages.PRODUCT_NOT_FOUND_FOR_GIVEN_ID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest extends MongoContainer {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Nested
    class CreateProductTests {
        @Test
        void shouldSaveOneNewProductToDatabase() {
            ProductResponse productResponse = productService.saveNewProduct(PRODUCT_REQUEST);
            checkSavedProduct(productResponse, PRODUCT_RESPONSE);
            List<Product> productList = productRepository.findAll();
            assertEquals(1, productList.size());
        }

        @Test
        void shouldSaveTwoNewProductToDatabase() {
            ProductResponse productResponse = productService.saveNewProduct(PRODUCT_REQUEST);
            ProductResponse productResponse1 = productService.saveNewProduct(PRODUCT_REQUEST1);
            checkSavedProduct(productResponse, PRODUCT_RESPONSE);
            checkSavedProduct(productResponse1, PRODUCT_RESPONSE1);
            List<Product> productList = productRepository.findAll();
            assertEquals(2, productList.size());
        }

        private void checkSavedProduct(ProductResponse productResponse, ProductResponse productResponse1) {
            assertNotNull(productResponse.getId());
            assertEquals(productResponse1.getProductCode(), productResponse.getProductCode());
            assertEquals(productResponse1.getProductName(), productResponse.getProductName());
            assertEquals(productResponse1.getTags(), productResponse.getTags());
            assertEquals(productResponse1.getCustomFields(), productResponse.getCustomFields());
            assertTrue(productRepository.existsById(productResponse.getId()));
        }

    }

    @Nested
    class GetProductsTest {
        @BeforeEach
        void setUp() {
            productRepository.save(PRODUCT);
            productRepository.save(PRODUCT1);
        }

        @Test
        void shouldGiveSingleSavedProductsFromFirstPage() {
            Page<ProductResponse> product = productService.getProduct(FIRST_PAGE_WITH_SIZE_ONE);
            assertEquals(1, product.getSize());
            assertEquals(PRODUCT.getProductCode(), product.getContent().get(0).getProductCode());
            assertEquals(PRODUCT.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT.getCustomFields(), product.getContent().get(0).getCustomFields());
        }

        @Test
        void shouldGiveTheSavedProductFromPage1Size2() {
            Page<ProductResponse> product = productService.getProduct(FIRST_PAGE_WITH_SIZE_TWO);
            assertEquals(2, product.getSize());
            assertEquals(PRODUCT.getProductCode(), product.getContent().get(0).getProductCode());
            assertEquals(PRODUCT.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT.getCustomFields(), product.getContent().get(0).getCustomFields());

            assertEquals(PRODUCT1.getProductCode(), product.getContent().get(1).getProductCode());
            assertEquals(PRODUCT1.getProductName(), product.getContent().get(1).getProductName());
            assertEquals(PRODUCT1.getPrice(), product.getContent().get(1).getPrice());
            assertEquals(PRODUCT1.getTags(), product.getContent().get(1).getTags());
            assertEquals(PRODUCT1.getCustomFields(), product.getContent().get(1).getCustomFields());
        }

        @Test
        void shouldGiveTheSavedProductFromPage1Size2SortedByName() {
            Page<ProductResponse> product = productService.getProduct(FIRST_PAGE_WITH_SIZE_TWO);
            assertEquals(2, product.getSize());
            assertEquals(PRODUCT.getProductCode(), product.getContent().get(0).getProductCode());
            assertEquals(PRODUCT.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT.getCustomFields(), product.getContent().get(0).getCustomFields());

            assertEquals(PRODUCT1.getProductCode(), product.getContent().get(1).getProductCode());
            assertEquals(PRODUCT1.getProductName(), product.getContent().get(1).getProductName());
            assertEquals(PRODUCT1.getPrice(), product.getContent().get(1).getPrice());
            assertEquals(PRODUCT1.getTags(), product.getContent().get(1).getTags());
            assertEquals(PRODUCT1.getCustomFields(), product.getContent().get(1).getCustomFields());
        }

        @Test
        void shouldGiveSavedUserFromPage2Size1() {
            Page<ProductResponse> product = productService.getProduct(SECOND_PAGE_WITH_SIZE_ONE);
            assertEquals(1, product.getSize());
            assertEquals(PRODUCT1.getProductCode(), product.getContent().get(0).getProductCode());
            assertEquals(PRODUCT1.getProductName(), product.getContent().get(0).getProductName());
            assertEquals(PRODUCT1.getPrice(), product.getContent().get(0).getPrice());
            assertEquals(PRODUCT1.getTags(), product.getContent().get(0).getTags());
            assertEquals(PRODUCT1.getCustomFields(), product.getContent().get(0).getCustomFields());
        }
    }


    @Nested
    class UpdateProductTests {

        Product savedProduct;
        Product savedProduct1;

        @BeforeEach
        void setUp() {
            productRepository.deleteAll();
            savedProduct = productRepository.save(PRODUCT);
            savedProduct1 = productRepository.save(PRODUCT1);
        }

        @Test
        void shouldUpdateTheProductWithGivenProductRequest() {
            String productId = savedProduct.getId();
            PRODUCT_RESPONSE1.setId(productId);
            ProductResponse productResponse = productService.updateProduct(productId, PRODUCT_REQUEST1);
            assertEquals(PRODUCT_RESPONSE1, productResponse);
        }

        @Test
        void shouldUpdateAnotherProductWithGivenProductRequest() {
            String productId = savedProduct1.getId();
            PRODUCT_RESPONSE.setId(productId);
            ProductResponse productResponse = productService.updateProduct(productId, PRODUCT_REQUEST);
            assertEquals(PRODUCT_RESPONSE, productResponse);
        }

        @Test
        void shouldGiveErrorWhenGivenProductIdIsNotFound() {
            PRODUCT_RESPONSE.setId(PRODUCT_ID);
            CustomException customException = assertThrows(CustomException.class, () -> productService.updateProduct(PRODUCT_ID, PRODUCT_REQUEST));
            assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
            assertEquals(ErrorMessages.PRODUCT_NOT_FOUND_FOR_GIVEN_ID, customException.getMessage());
        }
    }

    @Nested
    class DeleteProduct {
        Product savedProduct;
        Product savedProduct1;

        @BeforeEach
        void setUp() {
            productRepository.deleteAll();
            savedProduct = productRepository.save(PRODUCT);
            savedProduct1 = productRepository.save(PRODUCT1);
        }

        @Test
        void shouldDeleteTheProductWithGivenId() {
            String id = savedProduct.getId();
            productService.deleteProduct(id);
            assertFalse(productRepository.existsById(id));
        }

        @Test
        void shouldDeleteAnotherProductWithGivenId() {
            String id = savedProduct1.getId();
            productService.deleteProduct(id);
            assertFalse(productRepository.existsById(id));
        }

        @Test
        void shouldGiveErrorWhenProductWIthGivenIdNotExist() {
            CustomException customException = assertThrows(CustomException.class, () -> productService.deleteProduct(PRODUCT_ID));
            assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
            assertEquals(ErrorMessages.PRODUCT_NOT_FOUND_FOR_GIVEN_ID, customException.getMessage());
        }
    }

    @Nested
    class GetProductByIdTests {
        String productId;
        String productId1;

        @BeforeEach
        void setUp() {
            Product product = productRepository.save(PRODUCT);
            Product product1 = productRepository.save(PRODUCT1);
            productId = product.getId();
            productId1 = product1.getId();
            PRODUCT_RESPONSE.setId(productId);
            PRODUCT_RESPONSE1.setId(productId1);

        }

        @Test
        void shouldGiveTheSavedProductByID() {
            ProductResponse productResponse = productService.getProductByID(productId);
            assertEquals(PRODUCT_RESPONSE, productResponse);

        }

        @Test
        void shouldGiveAnotherProductByID() {
            ProductResponse productResponse = productService.getProductByID(productId1);
            assertEquals(PRODUCT_RESPONSE1, productResponse);
        }

        @Test
        void shouldGiveExceptionIfTheProductIsNotFoundWithGivenID() {
            CustomException customException = assertThrows(CustomException.class, () -> productService.getProductByID("productid"));
            assertEquals(PRODUCT_NOT_FOUND_FOR_GIVEN_ID, customException.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
        }
    }
}