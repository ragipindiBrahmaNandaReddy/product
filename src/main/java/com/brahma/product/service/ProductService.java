package com.brahma.product.service;

import com.brahma.product.bean.Product;
import com.brahma.product.bean.ProductResponse;
import com.brahma.product.exception.ProductAppException;
import com.brahma.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepository.class);
    @Autowired
    private ProductRepository repository;

    public Mono<ProductResponse> createProduct(Product product) {

        try {
            validateProduct(product);
            repository.save(product);
        } catch (ProductAppException e) {
            return Mono.error(new ProductAppException(e.getErrorMessage()));
        }
        return Mono.just(ProductResponse.builder().responseCode(0).responseMessage("ProductCreatedSuccessfully").build());
    }

    private Mono<ProductResponse> validateProduct(Product product) throws ProductAppException {

        if (product != null) {
            if (product.getProductId() == 0) {
                return Mono.error(new ProductAppException("product ID must provided"));
            }
            if (product.getProductName() == null) {
                return Mono.error(new ProductAppException("product is must"));
            }
            if (product.getProductQuantity() == 0) {
                LOGGER.info("Product quantity 0 leads to out of stock");
            }
        }
        return null;
    }

    public Mono<Product> getProductById(long id) {
        Optional<Product> byId;
        try {
            byId = repository.findById(id);
        } catch (DataAccessException exception) {
            return Mono.error(new ProductAppException("Exception occurred during fetching records from DB"));
        }
        Product product = Product.builder().build();
        if (byId.get()!=null) {
            product = byId.get();
        }else{
            LOGGER.info("Product not found with the provided id");
            return Mono.error(new ProductAppException("Product not found with the provided id"));
        }
        return Mono.just(product);
    }

    public Mono<Product> updateProduct(Product product) {

        Optional<Product> byId = repository.findById(product.getProductId());
        Product updateProduct = Product.builder().build();
        if(byId.get()==null) {
            updateProduct = byId.get();
        }else{
            Mono.error(new ProductAppException("product not found. Cannot update"));
        }
        updateProduct.setProductQuantity(product.getProductQuantity());
        updateProduct.setProductDescription(product.getProductDescription());
        repository.save(updateProduct);
        return Mono.just(updateProduct);
    }

    public Mono<ProductResponse> deleteProductById(long id) {
        repository.deleteById(id);
        return Mono.just(ProductResponse.builder().responseMessage("product deleted Successfully").responseCode(204).build());
    }

    public Mono<ProductResponse> getAllProducts() {

        List<Product> productList = repository.findAll();
        ProductResponse response = ProductResponse.builder().products(productList).build();
        return Mono.just(response);
    }
}
