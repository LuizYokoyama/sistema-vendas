package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.Valid;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FormDto {

    @EqualsAndHashCode.Include
    @Valid
    EventDto event;

    @Valid
    AdminDto admin;

    @Valid
    Set<StandDto> standsList;

}
