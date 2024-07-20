package com.brahma.product.configuration;

import com.brahma.product.handler.ProductHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.brahma.product.ProductConstants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class SpringConfiguration {


    @Autowired
    private  ProductHandler productHandler;

    @Bean
    public WebProperties.Resources resources(){
        return new WebProperties.Resources();
    }

    @Bean
    private RouterFunction<ServerResponse> compositeRout(){

        return route(POST(CREATE_PRODUCT_PATH).and(accept(MediaType.APPLICATION_NDJSON)), productHandler::createProduct)
                .andRoute(GET(GET_PRODUCT_BY_ID_PATH),productHandler::getProductById)
                .andRoute(PUT(UPDATE_PRODUCT_PATH).and(accept(MediaType.APPLICATION_NDJSON)), productHandler::updateProduct)
                .andRoute(DELETE(DELETE_PRODUCT_PATH), productHandler::deleteProductById)
                .andRoute(GET(GET_ALL_PRODUCT_PATH), productHandler::getAllProducts);
    }
}
