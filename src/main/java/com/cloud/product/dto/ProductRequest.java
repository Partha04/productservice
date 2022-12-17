package com.cloud.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotNull(message = "product name can not be empty")
    private String productName;
    @NotNull(message = "product price can not be empty")
    private Double price;
    private List<String> tags;
    @NotNull(message = "product code can not be empty")
    private String productCode;
    private Map<String, String> customFields;
}
