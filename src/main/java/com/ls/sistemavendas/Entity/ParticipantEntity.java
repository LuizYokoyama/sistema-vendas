package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable=false, updatable=false, nullable = false)
    private Timestamp entryDateTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private List<TransactionItemEntity> items;
}
