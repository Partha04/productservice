package com.cloud.product.service;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.model.Product;
import com.cloud.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    ModelMapper mapper = new ModelMapper();

    public ProductResponse saveNewProduct(ProductRequest productRequest) {
        Product entity = mapper.map(productRequest, Product.class);
        Product product = productRepository.save(entity);
        return mapper.map(product, ProductResponse.class);
    }

    public Page<ProductResponse> getProduct(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> mapper.map(product, ProductResponse.class));
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        return null;
    }
}
