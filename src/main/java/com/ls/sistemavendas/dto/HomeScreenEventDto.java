package com.ls.sistemavendas.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HomeScreenEventDto {

    @EqualsAndHashCode.Include
    private UUID id;

    private String eventName;

    private String photo;

    private String description;

}
