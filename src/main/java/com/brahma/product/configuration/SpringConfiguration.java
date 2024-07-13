package com.brahma.product.configuration;

import com.brahma.product.handler.ProductHandler;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.brahma.product.ProductConstants.CREATE_PRODUCT_PATH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@NoArgsConstructor
public class SpringConfiguration {


    @Autowired
    private  ProductHandler productHandler;

    public SpringConfiguration(final ProductHandler productHandler){
        this.productHandler = productHandler;
    }

    @Bean
    private RouterFunction<ServerResponse> compositeRout(){

        return route(POST(CREATE_PRODUCT_PATH).and(accept(MediaType.APPLICATION_NDJSON)), (HandlerFunction<ServerResponse>) productHandler::createProduct);
    }
}
