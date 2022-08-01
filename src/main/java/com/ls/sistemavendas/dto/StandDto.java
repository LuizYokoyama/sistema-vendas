package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandDto {

    public static final int STAND_DESCRIPTION_MAX_SIZE = 120;
    public static final int STAND_TOTAL_AGENT_MIN_VALUE = 1;
    public static final int STAND_TOTAL_AGENT_MAX_VALUE = 100;

    @EqualsAndHashCode.Include
    private long id;

    @NotNull(message = "Stand index should not be null")
    private int index;

    @Size(max = STAND_DESCRIPTION_MAX_SIZE, message = "Stand description size should not be greater then "
            + STAND_DESCRIPTION_MAX_SIZE)
    private String description;

    @NotNull(message = "Total agents should not be null")
    @Min(value = STAND_TOTAL_AGENT_MIN_VALUE, message = "Agents should not be less than " + STAND_TOTAL_AGENT_MIN_VALUE)
    @Max(value = STAND_TOTAL_AGENT_MAX_VALUE, message = "Agents should not be greater then " + STAND_TOTAL_AGENT_MAX_VALUE)
    private int totalAgents;

    @NotNull(message = "Products list should not be null")
    private Set<ProductDto> productsList;

}
