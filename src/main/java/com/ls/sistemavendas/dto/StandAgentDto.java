package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandAgentDto {

    public static final int NAME_MAX_SIZE = 80;

    @EqualsAndHashCode.Include
    private UUID id;

    @Size(max = NAME_MAX_SIZE)
    private String agentName;

}
