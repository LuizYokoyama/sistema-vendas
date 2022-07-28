package com.ls.sistemavendas.Entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="TB_EVENT")
@Data
public class EventEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String eventName;

    private int amountOfAgents;

    private LocalDate dateTimeFirstOccurrence;

    private float duration;
}
