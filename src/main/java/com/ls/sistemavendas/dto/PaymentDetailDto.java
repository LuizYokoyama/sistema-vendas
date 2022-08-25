package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

import static com.ls.sistemavendas.dto.ParticipantDto.CODE_MAX_SIZE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentDetailDto {

    private static final int COMMENT_MAX_SIZE = 50;
    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "Forneça o código do participante!")
    @Size(max = CODE_MAX_SIZE, message = "Forneça um código de no máximo " + CODE_MAX_SIZE + " caracteres!")
    private String participantCode;

    @Positive(message = "Forneça um valor positivo!")
    private double totalPayment;

    @Size(max = COMMENT_MAX_SIZE, message = "O comentário deve ter até "
            + COMMENT_MAX_SIZE + "caracteres!")
    private String comment;

    @Valid
    private Set<PaymentItemDetailDto> paymentItems;
}
