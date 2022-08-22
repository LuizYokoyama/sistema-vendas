package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionResponseDto {

    public static final int CODE_MAX_SIZE = 20;
    public static final int PASSWORD_MAX_SIZE = 8;

    @EqualsAndHashCode.Include
    @NotBlank(message = "Forneça o código do participante!")
    @Size(max = CODE_MAX_SIZE, message = "Forneça um código de no máximo " + CODE_MAX_SIZE + " caracteres!")
    private String participantCode;

    @NotBlank(message = "Informe um password!")
    @Size(max = PASSWORD_MAX_SIZE, message = "Informe um password de até " + PASSWORD_MAX_SIZE + "caracteres!")
    private String password;

    @PositiveOrZero(message = "Forneça um valor positivo!")
    private double totalTransaction;

    private UUID standId;

    @Valid
    private List<TransactionItemDto> items;

    private double standTotalTransactions;
}
