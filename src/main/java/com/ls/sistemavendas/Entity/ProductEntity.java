package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name="TB_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductEntity {

    public static final int PRODUCT_DESCRIPTION_MAX_SIZE = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @Size(max = PRODUCT_DESCRIPTION_MAX_SIZE, message = "Product description size should not be greater then "
            + PRODUCT_DESCRIPTION_MAX_SIZE)
    private String description;

    @Column(nullable = false)
    @PositiveOrZero
    private double price;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "stand_id")
    private StandEntity stand;

}
