package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionItemDto {

    @EqualsAndHashCode.Include
    @JsonIgnore
    private UUID id;

    @NotNull(message = "Forneça uma quantidade!")
    @Positive(message = "Forneça uma quantidade maior que zero!")
    private int quantity;

    private UUID productId;

    @FutureOrPresent(message = "Forneça uma data atual!")
    private LocalDateTime dateTime;

}

