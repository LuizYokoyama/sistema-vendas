package com.ls.sistemavendas.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FormDto {

    @EqualsAndHashCode.Include
    private EventDto event;

    private AdminDto admin;

    private Set<StandDto> standsList;

}
