package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="TB_EVENT")
@Data
public class EventEntity {


    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String name;

    private String photo;

    private String description;

    private int totalAgents;

    private LocalDateTime firstOccurrenceDateTime;

    private float duration;
}
