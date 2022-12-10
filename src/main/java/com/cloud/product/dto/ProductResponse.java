package com.cloud.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String productName;
    private Double price;
    private List<String> tags;
    private String productId;
    private Map<String, String> customFields;
}
