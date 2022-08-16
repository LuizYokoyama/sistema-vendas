package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TB_PARTICIPANT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantEntity {

    @Id
    @Column(name = "participant_id")
    @EqualsAndHashCode.Include
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "participant_id")
    private Set<TransactionItemEntity> items;
}
