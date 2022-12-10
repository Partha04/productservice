package com.cloud.product.controller;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping(path = "/product")
    ResponseEntity<ProductResponse> saveNewProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ResponseEntity<>(productService.saveNewProduct(productRequest), HttpStatus.CREATED);
    }

}
