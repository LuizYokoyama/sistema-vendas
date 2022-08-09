package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FormDetailsDto {

    @EqualsAndHashCode.Include
    @Valid
    EventDetailDto event;

    @Valid
    AdminDto admin;

    @Valid
    Set<StandDetailDto> standsList;

    @Valid
    Set<CashierAgentDto> agentsList;
}
