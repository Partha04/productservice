package com.cloud.product.controller;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.cloud.product.testData.ProductTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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
        String urlTemplate = "/new";

        @NotNull
        private ResultActions makeProductPostRequest(ProductRequest productRequest) throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(urlTemplate);
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
            productRequest.setProductCode("12");
            productRequest.setPrice(12.1);
            ResultActions resultActions = makeProductPostRequest(productRequest);
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("product name can not be empty"));
        }

        @Test
        void saveNewProductMustContainProductProductPrice() throws Exception {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setProductName("product name");
            productRequest.setProductCode("12");
            ResultActions resultActions = makeProductPostRequest(productRequest);
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("product price can not be empty"));
        }

        @Test
        void saveNewProductMustContainProductproductCode() throws Exception {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setProductName("product name");
            productRequest.setPrice(12.1);
            ResultActions resultActions = makeProductPostRequest(productRequest);
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("product code can not be empty"));
        }

        @Test
        void shouldGiveTheSavedProductWhenProductIsSavedSuccessFully() throws Exception {
            when(productService.saveNewProduct(PRODUCT_REQUEST)).thenReturn(PRODUCT_RESPONSE);
            ResultActions resultActions = makeProductPostRequest(PRODUCT_REQUEST);
            resultActions.andExpect(status().isCreated());
            resultActions.andExpect(jsonPath("productCode").value(PRODUCT_REQUEST.getProductCode()));
            resultActions.andExpect(jsonPath("productName").value(PRODUCT_REQUEST.getProductName()));
            resultActions.andExpect(jsonPath("price").value(PRODUCT_REQUEST.getPrice()));
            resultActions.andExpect(jsonPath("tags[0]").value(PRODUCT_REQUEST.getTags().get(0)));
            resultActions.andExpect(jsonPath("tags[1]").value(PRODUCT_REQUEST.getTags().get(1)));
            resultActions.andExpect(jsonPath("customFields").value(PRODUCT_REQUEST.getCustomFields()));
        }
    }

    @Nested
    class getProducts {
        String urlTemplate = "/all";

        @Test
        void shouldGiveTheListOfProduct() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate);
            ResultActions actions = mockMvc.perform(requestBuilder);
            actions.andExpect(status().isOk());
        }


        @Test
        void shouldInvokeTheProductService_getProductsMethod_with_Page_0_Size_10_Sort_id_and_Direction_ASC_ByDefault() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            when(productService.getProduct(Mockito.any(PageRequest.class))).thenReturn(null);

            mockMvc.perform(requestBuilder).andExpect(status().isOk());

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(productService, times(1)).getProduct(pageableCaptor.capture());
            PageRequest pageable = (PageRequest) pageableCaptor.getValue();
            assertEquals(0, pageable.getPageNumber());
            assertEquals(10, pageable.getPageSize());
            assertEquals("id: ASC", pageable.getSort().toString());
        }

        @Test
        void shouldInvokeTheProductService_getProductsMethod_with_Page_1_Size_3_Sort_name_and_Direction_DESC() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate);
            requestBuilder.param("page", "0");
            requestBuilder.param("size", "3");
            requestBuilder.param("sort", "name");
            requestBuilder.param("direction", "DESC");
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            when(productService.getProduct(Mockito.any(PageRequest.class))).thenReturn(null);

            mockMvc.perform(requestBuilder).andExpect(status().isOk());

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(productService, times(1)).getProduct(pageableCaptor.capture());
            PageRequest pageable = (PageRequest) pageableCaptor.getValue();
            assertEquals(0, pageable.getPageNumber());
            assertEquals(3, pageable.getPageSize());
            assertEquals("name: DESC", pageable.getSort().toString());
        }

        @Test
        void shouldInvokeTheProductService_getProductsMethod_with_Page_10_Size_1_Sort_size_and_Direction_ASC() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate);
            requestBuilder.param("page", "10");
            requestBuilder.param("size", "1");
            requestBuilder.param("sort", "size");
            requestBuilder.param("direction", "ASC");
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            when(productService.getProduct(Mockito.any(PageRequest.class))).thenReturn(null);

            mockMvc.perform(requestBuilder).andExpect(status().isOk());

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(productService, times(1)).getProduct(pageableCaptor.capture());
            PageRequest pageable = (PageRequest) pageableCaptor.getValue();
            assertEquals(10, pageable.getPageNumber());
            assertEquals(1, pageable.getPageSize());
            assertEquals("size: ASC", pageable.getSort().toString());
        }

        @Test
        void shouldGiveAPageWithOneProduct() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            List<ProductResponse> productResponseList = List.of(PRODUCT_RESPONSE);
            when(productService.getProduct(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(productResponseList));

            ResultActions actions = mockMvc.perform(requestBuilder).andExpect(status().isOk());

            actions.andExpect(status().isOk())
                    .andExpect(jsonPath("content[0].productName").value(PRODUCT_RESPONSE.getProductName()))
                    .andExpect(jsonPath("content[0].productCode").value(PRODUCT_RESPONSE.getProductCode()))
                    .andExpect(jsonPath("content[0].price").value(PRODUCT_RESPONSE.getPrice()))
                    .andExpect(jsonPath("content[0].price").value(PRODUCT_RESPONSE.getPrice()))
                    .andExpect(jsonPath("content[0].tags[0]").value(PRODUCT_RESPONSE.getTags().get(0)))
                    .andExpect(jsonPath("content[0].tags[1]").value(PRODUCT_RESPONSE.getTags().get(1)))
                    .andExpect(jsonPath("content[0].customFields.key1").value(PRODUCT_RESPONSE.getCustomFields().get("key1")))
            ;
        }

        @Test
        void shouldGiveAPageWithTwoProduct() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            List<ProductResponse> productResponseList = List.of(PRODUCT_RESPONSE, PRODUCT_RESPONSE1);
            when(productService.getProduct(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(productResponseList));

            ResultActions actions = mockMvc.perform(requestBuilder).andExpect(status().isOk());

            actions.andExpect(status().isOk())
                    .andExpect(jsonPath("content[0].productName").value(PRODUCT_RESPONSE.getProductName()))
                    .andExpect(jsonPath("content[0].productCode").value(PRODUCT_RESPONSE.getProductCode()))
                    .andExpect(jsonPath("content[0].price").value(PRODUCT_RESPONSE.getPrice()))
                    .andExpect(jsonPath("content[0].price").value(PRODUCT_RESPONSE.getPrice()))
                    .andExpect(jsonPath("content[0].tags[0]").value(PRODUCT_RESPONSE.getTags().get(0)))
                    .andExpect(jsonPath("content[0].tags[1]").value(PRODUCT_RESPONSE.getTags().get(1)))
                    .andExpect(jsonPath("content[0].customFields.key1").value(PRODUCT_RESPONSE.getCustomFields().get("key1")))

                    .andExpect(jsonPath("content[1].productName").value(PRODUCT_RESPONSE1.getProductName()))
                    .andExpect(jsonPath("content[1].productCode").value(PRODUCT_RESPONSE1.getProductCode()))
                    .andExpect(jsonPath("content[1].price").value(PRODUCT_RESPONSE1.getPrice()))
                    .andExpect(jsonPath("content[1].price").value(PRODUCT_RESPONSE1.getPrice()))
                    .andExpect(jsonPath("content[1].tags[0]").value(PRODUCT_RESPONSE1.getTags().get(0)))
                    .andExpect(jsonPath("content[1].tags[1]").value(PRODUCT_RESPONSE1.getTags().get(1)))
                    .andExpect(jsonPath("content[1].customFields.key2").value(PRODUCT_RESPONSE1.getCustomFields().get("key2")))
            ;
        }
    }
}