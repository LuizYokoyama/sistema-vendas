package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="TB_EVENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "event_name", nullable = false)
    private String name;

    private String photo;

    private String description;

    private int totalAgents;

    private LocalDateTime firstOccurrenceDateTime;

    private int duration;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Set<StandEntity> standsList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Set<EventAgentEntity> agentsList;

    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @Column(name = "admin_id", nullable = false)
    private String adminId;

    @Column(nullable = false, unique = true)
    private String login;

    private String avatar;

}
