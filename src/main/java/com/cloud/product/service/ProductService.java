package com.cloud.product.service;

import com.cloud.product.repository.ProductRepository;
import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    ModelMapper mapper = new ModelMapper();

    public ProductResponse saveNewProduct(ProductRequest productRequest) {
        Product product = productRepository.save(mapper.map(productRequest, Product.class));
        return mapper.map(product, ProductResponse.class);
    }
}
