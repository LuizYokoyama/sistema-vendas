package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String agentName;

    private String keycloakId;

}
