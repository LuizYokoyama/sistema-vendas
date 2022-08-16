package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionDto {

    public static final int NAME_MAX_SIZE = 80;
    public static final int CODE_MAX_SIZE = 30;
    public static final int PASSWORD_MAX_SIZE = 15;

    @EqualsAndHashCode.Include
    private UUID id;

   // private ParticipantDto participantDto;

    @JsonIgnore
    private boolean paid;

    @Valid
    private Set<TransactionItemDto> items;
}
