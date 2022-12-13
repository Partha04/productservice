package com.cloud.product.controller;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@EnableSpringDataWebSupport
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping(path = "/product")
    ResponseEntity<ProductResponse> saveNewProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ResponseEntity<>(productService.saveNewProduct(productRequest), HttpStatus.CREATED);
    }

    @GetMapping("/product")
    ResponseEntity<?> getProducts(@RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "size", defaultValue = "10") int size, @RequestParam(required = false, name = "sort", defaultValue = "id") String sort, @RequestParam(required = false, name = "direction", defaultValue = "ASC") String direction) {
        return ResponseEntity.ok(productService.getProduct(getPageable(page, size, sort, direction)));
    }

    private Pageable getPageable(int page, int size, String sort, String direction) {
        final String desc = "DESC";
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequest pageRequestWithSort = PageRequest.of(page, size, Objects.equals(direction, desc) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        return Objects.equals(sort, "") ? pageRequest : pageRequestWithSort;
    }
}
