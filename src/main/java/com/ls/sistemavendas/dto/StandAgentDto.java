package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandAgentDto {

    public static final int NAME_MAX_SIZE = 80;

    @EqualsAndHashCode.Include
    private String id;

    @Size(max = NAME_MAX_SIZE)
    private String agentName;

}
