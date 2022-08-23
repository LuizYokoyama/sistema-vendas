package com.ls.sistemavendas.dto;

import lombok.*;

import java.sql.Timestamp;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantSummaryDto {

    @EqualsAndHashCode.Include
    private String participantCode;

    private String name;

    private Timestamp timestamp;

    public ParticipantSummaryDto(String participantCode, String name, Object timestamp) {
        this.participantCode = participantCode;
        this.name = name;
        this.timestamp = (Timestamp) timestamp;
    }
}
