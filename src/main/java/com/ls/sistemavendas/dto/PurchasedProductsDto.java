package com.ls.sistemavendas.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PurchasedProductsDto {

    @EqualsAndHashCode.Include
    private String description;

    private double price;

    private int quantity;

    @EqualsAndHashCode.Include
    private Timestamp timestamp;

    public PurchasedProductsDto(String description, double price, int quantity, Object timestamp) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = (Timestamp) timestamp;
    }
}
