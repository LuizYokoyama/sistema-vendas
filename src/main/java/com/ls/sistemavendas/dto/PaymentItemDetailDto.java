package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentItemDetailDto {

    private static final int TYPE_PAYMENT_MAX_SIZE = 10;
    private static final int NOTE_PAYMENT_MAX_SIZE = 35;
    @EqualsAndHashCode.Include
    private UUID id;

    @Size(max = TYPE_PAYMENT_MAX_SIZE, message = "O tipo de pagamento deve ter até "
            + TYPE_PAYMENT_MAX_SIZE + "caracteres!")
    private String type;

    @Positive(message = "Forneça um valor positivo!")
    private double value;

    @Size(max = NOTE_PAYMENT_MAX_SIZE, message = "A observação de pagamento deve ter até "
            + NOTE_PAYMENT_MAX_SIZE + "caracteres!")
    private String note;
}
