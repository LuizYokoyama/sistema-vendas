package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TB_STAND")
@Data
public class StandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stand_id")
    @JsonIgnore
    private long id;

    private int index;

    private String description;

    private int totalAgents;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Set<ProductEntity> productsList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "event_id")
    private FormEntity form;

}
