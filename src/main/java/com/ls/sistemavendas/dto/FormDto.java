package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ls.sistemavendas.Entity.AdminEntity;
import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import lombok.Data;

import java.util.Set;

@Data
public class FormDto {

    @JsonIgnore
    private Long id;

    private EventEntity event;

    private AdminEntity admin;

    private Set<StandEntity> standsList;


}
