package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventDto {

    public static final int PHOTO_MAX_SIZE = 3145728;  // 3 Mb
    public static final int NAME_MAX_SIZE = 80;
    public static final int DESCRIPTION_MAX_SIZE = 160;
    public static final int TOTAL_AGENT_MIN_VALUE = 1;
    public static final int TOTAL_AGENT_MAX_VALUE = 1000;
    public static final int DURATION_MIN_VALUE = 1;
    public static final int DURATION_MAX_VALUE = 1000;

    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "Forneça o nome do evento!")
    @Size(max = NAME_MAX_SIZE)
    private String eventName;

    @Size(max = PHOTO_MAX_SIZE, message = "Utilize uma imagem de tamanho menor!")
    private String photo;

    @Size(max = DESCRIPTION_MAX_SIZE, message = "Escreva até "+DESCRIPTION_MAX_SIZE+" caracteres para a descrição!")
    private String description;

    @NotNull(message = "Forneça a quantidade de agentes do evento!")
    @Min(value = TOTAL_AGENT_MIN_VALUE, message = "Insira no mínimo "+ TOTAL_AGENT_MIN_VALUE + " agente!")
    @Max(value = TOTAL_AGENT_MAX_VALUE, message = "Insira no máximo "+ TOTAL_AGENT_MAX_VALUE + " agentes!")
    private int totalAgents;

    @FutureOrPresent(message = "Forneça uma data futura para o evento!")
    private LocalDateTime firstOccurrenceDateTime;

    @Min(value = DURATION_MIN_VALUE, message = "Forneça uma duração maior!")
    @Max(value = DURATION_MAX_VALUE, message = "Forneça uma duração menor!")
    private float duration;

}
