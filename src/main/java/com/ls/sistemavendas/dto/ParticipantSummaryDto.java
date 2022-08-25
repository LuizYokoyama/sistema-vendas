package com.ls.sistemavendas.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantSummaryDto {

    @EqualsAndHashCode.Include
    private String participantCode;

    private String name;

    private LocalDateTime timestamp;

    public ParticipantSummaryDto(String participantCode, String name, Object timestamp) {
        Timestamp timeStamp = (Timestamp) timestamp;
        this.participantCode = participantCode;
        this.name = name;
        this.timestamp = timeStamp.toLocalDateTime();
    }
}
