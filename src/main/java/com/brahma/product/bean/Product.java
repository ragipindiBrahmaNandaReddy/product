package com.brahma.product.bean;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "rr_product")
public class Product {
    @Id
    private long productId;
    private String productName;
    private String productDescription;
    private long productQuantity;
}
