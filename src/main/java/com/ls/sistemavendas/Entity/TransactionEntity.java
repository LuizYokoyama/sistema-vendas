package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_TRANSACTION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    @EqualsAndHashCode.Include
    private UUID id;
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    @JsonIgnore
    private ParticipantEntity participant;*/

    private boolean paid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "transaction_id")
    private Set<TransactionItemEntity> items;
}
