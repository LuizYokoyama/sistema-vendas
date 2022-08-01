package com.ls.sistemavendas.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandDto {

    @EqualsAndHashCode.Include
    private long id;

    private int index;

    private String description;

    private int totalAgents;

    private Set<ProductDto> productsList;

}
