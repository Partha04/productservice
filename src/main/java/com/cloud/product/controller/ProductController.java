package com.cloud.product.controller;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/all")
    ResponseEntity<Page<ProductResponse>> getProducts(@RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "size", defaultValue = "10") int size, @RequestParam(required = false, name = "sort", defaultValue = "id") String sort, @RequestParam(required = false, name = "direction", defaultValue = "ASC") String direction) {
        Pageable pageRequest = getPageRequest(page, size, sort, direction);
        Page<ProductResponse> product = productService.getProduct(pageRequest);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/new")
    ResponseEntity<ProductResponse> saveNewProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ResponseEntity<>(productService.saveNewProduct(productRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable(required = true, name = "id") String id) {
        return new ResponseEntity<>(productService.getProductByID(id), HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable(name = "id") String id, @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.updateProduct(id, productRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    private Pageable getPageRequest(int page, int size, String sort, String direction) {
        final String desc = "DESC";
        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequest pageRequestWithSort = PageRequest.of(page, size, Objects.equals(direction, desc) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        return Objects.equals(sort, "") ? pageRequest : pageRequestWithSort;
    }
}
