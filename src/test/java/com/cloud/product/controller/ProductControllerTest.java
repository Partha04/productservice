package com.cloud.product.controller;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.cloud.product.testData.ProductTestData.PRODUCT_REQUEST;
import static com.cloud.product.testData.ProductTestData.PRODUCT_RESPONSE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    ProductService productService;

    @Nested
    class SaveProduct {

        @NotNull
        private ResultActions makeProductPostRequest(ProductRequest productRequest) throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/product");
            String content = objectMapper.writeValueAsString(productRequest);
            requestBuilder.content(content);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            return mockMvc.perform(requestBuilder);
        }

        @Test
        void shouldSaveAProductSuccessFully() throws Exception {
            ResultActions resultActions = makeProductPostRequest(PRODUCT_REQUEST);
            resultActions.andExpect(status().isCreated());
        }

        @Test
        void shouldInvokeProductService_saveNewProductMethod() throws Exception {

            makeProductPostRequest(PRODUCT_REQUEST);
            verify(productService).saveNewProduct(PRODUCT_REQUEST);
        }

        @Test
        void saveNewProductMustContainProductName() throws Exception {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setProductId("12");
            productRequest.setPrice(12.1);
            ResultActions resultActions = makeProductPostRequest(productRequest);
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("product name can not be empty"));
        }

        @Test
        void saveNewProductMustContainProductProductPrice() throws Exception {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setProductName("product name");
            productRequest.setProductId("12");
            ResultActions resultActions = makeProductPostRequest(productRequest);
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("product price can not be empty"));
        }

        @Test
        void saveNewProductMustContainProductProductID() throws Exception {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setProductName("product name");
            productRequest.setPrice(12.1);
            ResultActions resultActions = makeProductPostRequest(productRequest);
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("product ID can not be empty"));
        }

        @Test
        void shouldGiveTheSavedProductWhenProductIsSavedSuccessFully() throws Exception {
            when(productService.saveNewProduct(PRODUCT_REQUEST)).thenReturn(PRODUCT_RESPONSE);
            ResultActions resultActions = makeProductPostRequest(PRODUCT_REQUEST);
            resultActions.andExpect(status().isCreated());
            resultActions.andExpect(jsonPath("productId").value(PRODUCT_REQUEST.getProductId()));
            resultActions.andExpect(jsonPath("productName").value(PRODUCT_REQUEST.getProductName()));
            resultActions.andExpect(jsonPath("price").value(PRODUCT_REQUEST.getPrice()));
            resultActions.andExpect(jsonPath("tags[0]").value(PRODUCT_REQUEST.getTags().get(0)));
            resultActions.andExpect(jsonPath("tags[1]").value(PRODUCT_REQUEST.getTags().get(1)));
            resultActions.andExpect(jsonPath("customFields").value(PRODUCT_REQUEST.getCustomFields()));
        }
    }
}