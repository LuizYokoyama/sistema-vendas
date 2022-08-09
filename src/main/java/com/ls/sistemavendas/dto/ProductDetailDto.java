package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductDetailDto {

    public static final int PRODUCT_DESCRIPTION_MAX_SIZE = 150;

    @EqualsAndHashCode.Include
    private UUID id;

    @Size(max = PRODUCT_DESCRIPTION_MAX_SIZE, message = "A descrição do produto deve ter até "
            + PRODUCT_DESCRIPTION_MAX_SIZE + "caracteres!")
    private String description;

    @PositiveOrZero(message = "O preço deve ser positivo!")
    private double price;

}
