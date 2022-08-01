package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FormDto {

    @EqualsAndHashCode.Include
    @NotNull(message = "Events should not be null")
    private EventDto event;

    @NotNull(message = "Admin should not be null")
    private AdminDto admin;

    @NotNull(message = "Stands should not be null")
    private Set<StandDto> standsList;

}
