package com.cloud.product.testData;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.model.Product;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;

@Configuration
public class ProductTestData {

    public static ProductRequest PRODUCT_REQUEST = new ProductRequest();
    public static ProductResponse PRODUCT_RESPONSE = new ProductResponse();

    public static ProductRequest PRODUCT_REQUEST1 = new ProductRequest();
    public static ProductResponse PRODUCT_RESPONSE1 = new ProductResponse();
    public static PageRequest FIRST_PAGE_WITH_SIZE_ONE = PageRequest.of(0, 1);
    public static PageRequest FIRST_PAGE_WITH_SIZE_TWO = PageRequest.of(0, 2);
    public static PageRequest SECOND_PAGE_WITH_SIZE_ONE = PageRequest.of(1, 1);
    public static PageRequest SECOND_PAGE_WITH_SIZE_ONE_SORTED_BY_NAME = PageRequest.of(1, 1, Sort.by("name").ascending());
    public static Product PRODUCT = new Product();
    public static Product PRODUCT1 = new Product();

    public static String PRODUCT_ID = "639d77e2b481e31d80294ecc";


    static {
        String product_name = "product Name";
        String product_code = "product code";
        List<String> tags = List.of("tag1", "tage2");
        HashMap<String, String> requestCustomFields = new HashMap<>();
        requestCustomFields.put("key1", "value1");
        double price = 150.9;

        PRODUCT.setProductCode(product_code);
        PRODUCT.setProductName(product_name);
        PRODUCT.setTags(tags);
        PRODUCT.setPrice(price);
        PRODUCT.setCustomFields(requestCustomFields);

        PRODUCT_REQUEST.setProductName(product_name);
        PRODUCT_REQUEST.setProductCode(product_code);
        PRODUCT_REQUEST.setTags(tags);
        PRODUCT_REQUEST.setCustomFields(requestCustomFields);
        PRODUCT_REQUEST.setPrice(price);

        PRODUCT_RESPONSE.setProductName(product_name);
        PRODUCT_RESPONSE.setProductCode(product_code);
        PRODUCT_RESPONSE.setTags(tags);
        PRODUCT_RESPONSE.setCustomFields(requestCustomFields);
        PRODUCT_RESPONSE.setPrice(price);


        String product_name1 = "product Name 1";
        String product_code1 = "product code 1";
        List<String> tags1 = List.of("tag3", "tage4");
        HashMap<String, String> requestCustomFields1 = new HashMap<>();
        requestCustomFields1.put("key2", "value2");
        double price1 = 140.1;


        PRODUCT1.setProductCode(product_code1);
        PRODUCT1.setProductName(product_name1);
        PRODUCT1.setTags(tags1);
        PRODUCT1.setPrice(price1);
        PRODUCT1.setCustomFields(requestCustomFields1);

        PRODUCT_REQUEST1.setProductName(product_name1);
        PRODUCT_REQUEST1.setProductCode(product_code1);
        PRODUCT_REQUEST1.setTags(tags1);
        PRODUCT_REQUEST1.setCustomFields(requestCustomFields1);
        PRODUCT_REQUEST1.setPrice(price1);

        PRODUCT_RESPONSE1.setProductName(product_name1);
        PRODUCT_RESPONSE1.setProductCode(product_code1);
        PRODUCT_RESPONSE1.setTags(tags1);
        PRODUCT_RESPONSE1.setCustomFields(requestCustomFields1);
        PRODUCT_RESPONSE1.setPrice(price1);
    }
}
