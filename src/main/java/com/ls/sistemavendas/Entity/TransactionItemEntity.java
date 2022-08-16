package com.ls.sistemavendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_ITEM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "product_id", nullable = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "product_FK"), name = "product_id", referencedColumnName = "product_id")
    private UUID product;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @JoinColumn(foreignKey = @ForeignKey(name = "participant_FK"),
            name = "participant_id", referencedColumnName = "participant_id")
    @JsonIgnore
    @Column(name = "participant_id", nullable = false)
    private String participantCode;

}
