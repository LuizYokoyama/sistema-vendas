package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantDto {

    public static final int NAME_MAX_SIZE = 80;
    public static final int CODE_MAX_SIZE = 30;
    public static final int PASSWORD_MAX_SIZE = 15;

    @EqualsAndHashCode.Include
    private UUID id;

    @Size(max = CODE_MAX_SIZE)
    private String code;

    @NotBlank(message = "Forneça o nome do participante!")
    @Size(max = NAME_MAX_SIZE)
    private String name;

    @NotBlank(message = "Informe um password!")
    @Size(max = PASSWORD_MAX_SIZE, message = "Informe um password de até " + PASSWORD_MAX_SIZE + "caracteres!")
    private String password;

    @Valid
    private TransactionDto transactions;
}
