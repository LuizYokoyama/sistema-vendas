package com.ls.sistemavendas.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductDto {



    @EqualsAndHashCode.Include
    private Long id;

    private String description;

    private double price;

}
