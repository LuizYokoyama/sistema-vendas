package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantDetailDto {

    @EqualsAndHashCode.Include
    private String participantCode;

    private String name;

    private String password;

    @JsonIgnore
    private Timestamp entryDateTime;

    @JsonIgnore
    private Set<TransactionItemDto> items;
}
