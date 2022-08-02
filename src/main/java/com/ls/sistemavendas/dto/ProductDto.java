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

    @Size(max = PRODUCT_DESCRIPTION_MAX_SIZE, message = "A descrição do produto deve ter até "
            + PRODUCT_DESCRIPTION_MAX_SIZE + "caracteres!")
    private String description;

    @PositiveOrZero(message = "O preço deve ser positivo!")
    private double price;

}
