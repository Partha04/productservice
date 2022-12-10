package com.cloud.product.testData;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;

import java.util.HashMap;
import java.util.List;

public class ProductTestData {

    public static ProductRequest PRODUCT_REQUEST = new ProductRequest();
    public static ProductResponse PRODUCT_RESPONSE = new ProductResponse();

    public static ProductRequest PRODUCT_REQUEST1 = new ProductRequest();
    public static ProductResponse PRODUCT_RESPONSE1 = new ProductResponse();

    static {
        String product_name = "product Name";
        String product_id = "product id";
        List<String> tags = List.of("tag1", "tage2");
        HashMap<String, String> requestCustomFields = new HashMap<>();
        requestCustomFields.put("key1", "value1");
        double price = 50.9;

        PRODUCT_REQUEST.setProductName(product_name);
        PRODUCT_REQUEST.setProductId(product_id);
        PRODUCT_REQUEST.setTags(tags);
        PRODUCT_REQUEST.setCustomFields(requestCustomFields);
        PRODUCT_REQUEST.setPrice(price);

        PRODUCT_RESPONSE.setProductName(product_name);
        PRODUCT_RESPONSE.setProductId(product_id);
        PRODUCT_RESPONSE.setTags(tags);
        PRODUCT_RESPONSE.setCustomFields(requestCustomFields);
        PRODUCT_RESPONSE.setPrice(price);


        String product_name1 = "product Name 1";
        String product_id1 = "product id 1";
        List<String> tags1 = List.of("tag3", "tage4");
        HashMap<String, String> requestCustomFields1 = new HashMap<>();
        requestCustomFields.put("key2", "value2");
        double price1 = 150.1;


        PRODUCT_REQUEST1.setProductName(product_name1);
        PRODUCT_REQUEST1.setProductId(product_id1);
        PRODUCT_REQUEST1.setTags(tags1);
        PRODUCT_REQUEST1.setCustomFields(requestCustomFields1);
        PRODUCT_REQUEST1.setPrice(price1);

        PRODUCT_RESPONSE1.setProductName(product_name1);
        PRODUCT_RESPONSE1.setProductId(product_id1);
        PRODUCT_RESPONSE1.setTags(tags1);
        PRODUCT_RESPONSE1.setCustomFields(requestCustomFields1);
        PRODUCT_RESPONSE1.setPrice(price1);
    }
}
