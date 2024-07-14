package com.brahma.product.handler;

import com.brahma.product.bean.Product;
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
                .onErrorResume(e -> {
                    LOGGER.error("Error processing request: {}", e.getMessage());
                    return ServerResponse.status(500).bodyValue("Internal Server Error");
                });
    }


    public Mono<ServerResponse> getProductById(final ServerRequest serverRequest) {

        return productService.getProductById(serverRequest.pathVariable("id"))
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> {
                    LOGGER.error("Error processing request: {}", e.getMessage());
                    return ServerResponse.status(500).bodyValue("Internal Server Error");
                });
    }
}
