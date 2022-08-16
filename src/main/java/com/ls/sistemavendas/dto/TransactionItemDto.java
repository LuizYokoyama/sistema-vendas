package com.ls.sistemavendas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
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

    @NotNull
    private UUID productID;

    @FutureOrPresent(message = "Forneça a data atual!")
    @JsonIgnore
    private Timestamp dateTime;

}

