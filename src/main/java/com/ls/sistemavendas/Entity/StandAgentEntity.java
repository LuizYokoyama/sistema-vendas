package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="TB_STAND_AGENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StandAgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "agent_id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "agent_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stand_id")
    @JsonIgnore
    private StandEntity stand;
}
