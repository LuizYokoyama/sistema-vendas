package com.ls.sistemavendas.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private LocalDateTime timestamp;

    public PurchasedProductsDto(String description, double price, int quantity, Object timestamp) {
        Timestamp timeStamp = (Timestamp) timestamp;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timeStamp.toLocalDateTime();
    }
}
