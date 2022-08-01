package com.ls.sistemavendas.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HomeScreenEventDto {

    @EqualsAndHashCode.Include
    private Long id;

    private String eventName;

    private String photo;

    private String description;

}
