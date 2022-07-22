package com.ls.sistemavendas.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName;
}
