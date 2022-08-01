package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="TB_EVENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String name;

    private String photo;

    private String description;

    private int totalAgents;

    private LocalDateTime firstOccurrenceDateTime;

    private float duration;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stand_id")
    private Set<StandEntity> standsList;

    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    private String avatar;

}
