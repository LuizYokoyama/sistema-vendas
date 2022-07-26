package com.ls.sistemavendas.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name="TB_EVENT")
@Data
public class Event implements Serializable {

    private final static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false )
    private String eventName;

    private int amountOfAgents;

    private Date dateFirstOccurrence;

    private Time timeFirstOccurrence;

    private float duration;
}
