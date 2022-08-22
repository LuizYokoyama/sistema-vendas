package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionDto {

    @EqualsAndHashCode.Include
    private String participantCode;

    private String password;

    private double totalTransaction;

    @Valid
    private List<TransactionItemDto> items;
}
