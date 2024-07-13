package com.brahma.product.service;

import com.brahma.product.bean.Product;
import com.brahma.product.bean.ProductResponse;
import com.brahma.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Mono<ProductResponse> createProduct(Product product) {

        validateProduct(product);

        repository.save(product);

        return Mono.just(ProductResponse.builder().responseCode(0).responseMessage("ProductCreatedSuccessfully").build());
    }

    private void validateProduct(Product product) {

    }
}
