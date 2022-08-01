package com.ls.sistemavendas.Entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    public static final int AVATAR_MAX_SIZE = 3145728;  // 3 Mb
    public static final int NAME_MAX_SIZE = 80;
    public static final int LOGIN_MAX_SIZE = 30;
    public static final int PASSWORD_MAX_SIZE = 15;
    public static final int PHOTO_MAX_SIZE = 3145728;  // 3 Mb
    public static final int TOTAL_AGENT_MIN_VALUE = 1;
    public static final int TOTAL_AGENT_MAX_VALUE = 1000;
    public static final int DURATION_MIN_VALUE = 1;
    public static final int DURATION_MAX_VALUE = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "event_name", nullable = false)
    @NotNull(message = "The event name cannot be null")
    @Size(max = NAME_MAX_SIZE)
    private String name;

    @Size(max = PHOTO_MAX_SIZE, message = "Image size limit exceeded")
    private String photo;

    private String description;

    @Min(value = TOTAL_AGENT_MIN_VALUE)
    @Max(value = TOTAL_AGENT_MAX_VALUE)
    private int totalAgents;

    @FutureOrPresent
    private LocalDateTime firstOccurrenceDateTime;

    @Min(value = DURATION_MIN_VALUE)
    @Max(value = DURATION_MAX_VALUE)
    private float duration;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stand_id")
    @NotNull(message = "Stands should not be null")
    private Set<StandEntity> standsList;

    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @Column(nullable = false)
    @NotNull(message = "The login cannot be null")
    @Size(max = LOGIN_MAX_SIZE, message = "Login size should not be greater then " + LOGIN_MAX_SIZE)
    private String login;

    @Column(nullable = false)
    @NotNull(message = "The password cannot be null")
    @Size(max = PASSWORD_MAX_SIZE, message = "Password size should not be greater then " + PASSWORD_MAX_SIZE)
    private String password;

    @Size(max = AVATAR_MAX_SIZE, message = "Image size limit exceeded")
    private String avatar;

}
