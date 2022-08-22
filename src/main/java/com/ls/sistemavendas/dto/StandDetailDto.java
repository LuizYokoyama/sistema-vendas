package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandDetailDto {

    public static final int STAND_DESCRIPTION_MAX_SIZE = 150;
    public static final int STAND_TOTAL_AGENT_MIN_VALUE = 1;
    public static final int STAND_TOTAL_AGENT_MAX_VALUE = 900;

    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull(message = "Forneça um índice!")
    @Positive(message = "Forneça um índice maior que zero!")
    private int index;

    @Size(max = STAND_DESCRIPTION_MAX_SIZE, message = "A descrição deve ter até " + STAND_DESCRIPTION_MAX_SIZE +
            " caracteres!")
    @NotBlank(message = "Forneça a descrição da barraca!")
    private String description;

    @NotNull(message = "Forneça a quantidade de agentes desta barraca!")
    @Min(value = STAND_TOTAL_AGENT_MIN_VALUE, message = "Insira no mínimo " + STAND_TOTAL_AGENT_MIN_VALUE + " agente!")
    @Max(value = STAND_TOTAL_AGENT_MAX_VALUE, message = "Insira no máximo " + STAND_TOTAL_AGENT_MAX_VALUE + " agentes!")
    private int standTotalAgents;

    @PositiveOrZero(message = "Forneça um valor positivo!")
    private double totalTransactions;

    @Valid
    private Set<ProductDetailDto> productsList;

    @Valid
    private Set<StandAgentDto> agentsList;
}
