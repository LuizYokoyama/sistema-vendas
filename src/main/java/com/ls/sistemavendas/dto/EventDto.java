package com.ls.sistemavendas.dto;

import com.ls.sistemavendas.Entity.StandEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventDto {

    private Long id;

    private String eventName;

    private String photo;

    private String description;

    private int totalAgents;

    private LocalDateTime firstOccurrenceDateTime;

    private float duration;

    private Set<StandEntity> standEntitySet;
}
