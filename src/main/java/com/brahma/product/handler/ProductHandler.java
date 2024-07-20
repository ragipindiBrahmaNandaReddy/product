package com.brahma.product.handler;

import com.brahma.product.bean.Product;
import com.brahma.product.bean.ProductResponse;
import com.brahma.product.exception.ProductAppException;
import com.brahma.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHandler.class);

    @Autowired
    private ProductService productService;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    LOGGER.info("Received product details: {}", product);
                    return productService.createProduct(product);
                })
                .flatMap(savedProduct -> {
                    LOGGER.info("Saved product details: {}", savedProduct);
                    return ServerResponse.ok().bodyValue(savedProduct);
                })
                .onErrorResume(Mono::error);

    }


    public Mono<ServerResponse> getProductById(final ServerRequest serverRequest) {

        return productService.getProductById(Integer.parseInt(serverRequest.pathVariable("id")))
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(Mono::error);
    }


    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    LOGGER.info("Received product details: {}", product);
                    return productService.updateProduct(product).onErrorResume(Mono::error);
                })
                .flatMap(savedProduct -> {
                    LOGGER.info("Saved product details: {}", savedProduct);
                    return ServerResponse.ok().bodyValue(savedProduct);
                });

    }

    public Mono<ServerResponse> deleteProductById(final ServerRequest serverRequest) {

        return productService.deleteProductById(Long.parseLong(serverRequest.pathVariable("id")))
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(Mono::error);
    }

    public Mono<ServerResponse> getAllProducts(ServerRequest request){
        return productService.getAllProducts()
        .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }
}
