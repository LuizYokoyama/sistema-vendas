package com.ls.sistemavendas.dto;

import com.ls.sistemavendas.Entity.StandEntity;
import lombok.Data;

@Data
public class ProductDto {

    private Long id;

    private String description;

    private double price;

    private StandEntity stand;

}
