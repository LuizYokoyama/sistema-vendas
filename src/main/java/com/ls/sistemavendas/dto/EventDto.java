package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventDto {

    public static final int PHOTO_MAX_SIZE = 3145728;  // 3 Mb
    public static final int TOTAL_AGENT_MIN_VALUE = 1;
    public static final int TOTAL_AGENT_MAX_VALUE = 1000;

    public static final int DURATION_MIN_VALUE = 1;

    public static final int DURATION_MAX_VALUE = 1000;

    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "The event name cannot be null")
    private String eventName;

    @Size(max = PHOTO_MAX_SIZE, message = "Image size limit exceeded")
    private String photo;

    private String description;

    @Min(value = TOTAL_AGENT_MIN_VALUE, message = "Agents should not be less than " + TOTAL_AGENT_MIN_VALUE)
    @Max(value = TOTAL_AGENT_MAX_VALUE, message = "Agents should not be greater then " + TOTAL_AGENT_MAX_VALUE)
    private int totalAgents;

    @FutureOrPresent
    private LocalDateTime firstOccurrenceDateTime;

    @Min(value = DURATION_MIN_VALUE, message = "Duration should not be less than " + DURATION_MIN_VALUE)
    @Max(value = DURATION_MAX_VALUE, message = "Duration should not be greater then " + DURATION_MAX_VALUE)
    private float duration;

}
