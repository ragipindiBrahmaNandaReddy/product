package com.brahma.product.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private int responseCode;
    private String responseMessage;
}
