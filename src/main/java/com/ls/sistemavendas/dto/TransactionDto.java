package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionDto {

    @EqualsAndHashCode.Include
    private String participantCode;

    @Valid
    private Set<TransactionItemDto> items;
}
