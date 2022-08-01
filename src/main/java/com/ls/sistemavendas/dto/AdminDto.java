package com.ls.sistemavendas.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdminDto {



    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Include
    private String login;

    private String password;

    private String avatar;
}
