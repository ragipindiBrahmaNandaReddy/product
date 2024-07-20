package com.brahma.product.exception;

import lombok.Getter;

@Getter
public class ProductAppException extends Exception {
    private String errorMessage;
    public ProductAppException(String message) {
        super(message);

    }
}
