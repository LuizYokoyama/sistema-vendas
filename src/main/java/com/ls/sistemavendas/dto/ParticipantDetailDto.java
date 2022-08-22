package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantDetailDto {

    public static final int NAME_MAX_SIZE = 40;
    public static final int CODE_MAX_SIZE = 20;
    public static final int PASSWORD_MAX_SIZE = 8;

    @EqualsAndHashCode.Include
    @NotBlank(message = "Forneça o código do participante!")
    @Size(max = CODE_MAX_SIZE, message = "Forneça um código de no máximo " + CODE_MAX_SIZE + " caracteres!")
    private String participantCode;

    @NotBlank(message = "Forneça o nome do participante!")
    @Size(max = NAME_MAX_SIZE, message = "Forneça um nome de no máximo " + NAME_MAX_SIZE + " caracteres!")
    private String name;

    @NotBlank(message = "Informe um password!")
    @Size(max = PASSWORD_MAX_SIZE, message = "Informe um password de até " + PASSWORD_MAX_SIZE + "caracteres!")
    private String password;

    @PastOrPresent(message = "Forneça a data atual!")
    @JsonIgnore
    private Timestamp entryDateTime;

    @Valid
    @JsonIgnore
    private Set<TransactionItemDto> items;
}
