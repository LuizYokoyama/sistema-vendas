package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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
    @Column(name = "agent_id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "agent_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stand_id")
    @JsonIgnore
    private StandEntity stand;

    @Column(name = "keycloak_id", nullable = false)
    private String keycloakId;
}
