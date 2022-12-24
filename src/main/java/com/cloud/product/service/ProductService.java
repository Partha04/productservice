package com.cloud.product.service;

import com.cloud.product.dto.ProductRequest;
import com.cloud.product.dto.ProductResponse;
import com.cloud.product.exception.CustomException;
import com.cloud.product.model.Product;
import com.cloud.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.cloud.product.util.ErrorMessages.PRODUCT_NOT_FOUND_FOR_GIVEN_ID;

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
        boolean existsById = productRepository.existsById(id);
        if (!existsById) throw new CustomException(PRODUCT_NOT_FOUND_FOR_GIVEN_ID, HttpStatus.NOT_FOUND);
        Product updatedProduct = mapper.map(productRequest, Product.class);
        updatedProduct.setId(id);
        Product product = productRepository.save(updatedProduct);
        return mapper.map(product, ProductResponse.class);
    }

    public void deleteProduct(String id) {
        if (productRepository.existsById(id)) productRepository.deleteById(id);
        else throw new CustomException(PRODUCT_NOT_FOUND_FOR_GIVEN_ID, HttpStatus.NOT_FOUND);
    }

    public ProductResponse getProductByID(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) throw new CustomException(PRODUCT_NOT_FOUND_FOR_GIVEN_ID, HttpStatus.NOT_FOUND);
        return mapper.map(optionalProduct.get(), ProductResponse.class);
    }
}
