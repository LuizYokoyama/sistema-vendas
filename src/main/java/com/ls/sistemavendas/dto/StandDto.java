package com.ls.sistemavendas.dto;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.Entity.ProductEntity;
import lombok.Data;

import java.util.Set;

@Data
public class StandDto {

    private long id;

    private int index;

    private String description;

    private int totalAgents;

    private Set<ProductEntity> productsList;

    private EventEntity eventEntity;
}
