package com.brahma.product.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.brahma.product.ProductConstants.*;
import static com.brahma.product.ProductConstants.DELETE_PRODUCT_PATH;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class ProductAppExceptionHandler extends AbstractErrorWebExceptionHandler {


    public ProductAppExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                   ApplicationContext applicationContext, ServerCodecConfigurer configure) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(configure.getReaders());
        this.setMessageWriters(configure.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.POST(CREATE_PRODUCT_PATH), this::handleException)
                .andRoute(RequestPredicates.GET(GET_PRODUCT_BY_ID_PATH),this::handleException)
                .andRoute(RequestPredicates.PUT(UPDATE_PRODUCT_PATH).and(accept(MediaType.APPLICATION_NDJSON)), this::handleException)
                .andRoute(RequestPredicates.DELETE(DELETE_PRODUCT_PATH), this::handleException)
                .andRoute(RequestPredicates.GET(GET_ALL_PRODUCT_PATH),this::handleException);
    }


    private Mono<ServerResponse> handleException(ServerRequest serverRequest) {
        Throwable th = getError(serverRequest);
        Map<String, Object> error = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
    }
}
