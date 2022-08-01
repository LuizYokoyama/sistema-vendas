package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductDto {

    public static final int PRODUCT_DESCRIPTION_MAX_SIZE = 120;

    @EqualsAndHashCode.Include
    private Long id;

    @Size(max = PRODUCT_DESCRIPTION_MAX_SIZE, message = "Product description size should not be greater then "
            + PRODUCT_DESCRIPTION_MAX_SIZE)
    private String description;

    @PositiveOrZero
    private double price;

}
