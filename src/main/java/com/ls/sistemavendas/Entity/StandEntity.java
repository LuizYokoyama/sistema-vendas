package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "TB_STAND")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandEntity {

    public static final int STAND_DESCRIPTION_MAX_SIZE = 120;
    public static final int STAND_TOTAL_AGENT_MIN_VALUE = 1;
    public static final int STAND_TOTAL_AGENT_MAX_VALUE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stand_id")
    @EqualsAndHashCode.Include
    private long id;

    @NotNull(message = "Stand index should not be null")
    private int index;

    @Size(max = STAND_DESCRIPTION_MAX_SIZE)
    private String description;

    @NotNull(message = "Total agents of the stand should not be null")
    @Min(value = STAND_TOTAL_AGENT_MIN_VALUE)
    @Max(value = STAND_TOTAL_AGENT_MAX_VALUE)
    private int totalAgents;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull(message = "Products list should not be null")
    private Set<ProductEntity> productsList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "event_id")
    private EventEntity event;

}
