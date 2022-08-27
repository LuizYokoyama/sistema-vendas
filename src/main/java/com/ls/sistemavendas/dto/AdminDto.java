package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdminDto {

    public static final int ADM_NAME_MAX_SIZE = 80;
    public static final int AVATAR_MAX_SIZE = 3145728;  // 3 Mb
    public static final int LOGIN_MAX_SIZE = 30;
    public static final int PASSWORD_MAX_SIZE = 15;

    @EqualsAndHashCode.Include
    @NotBlank(message = "Forneça o nome do administrador do evento!")
    @Size(max = AVATAR_MAX_SIZE)
    private String name;

    @EqualsAndHashCode.Include
    @NotBlank(message = "Forneça um login!")
    @Size(max = LOGIN_MAX_SIZE, message = "Utilize um login de até " + LOGIN_MAX_SIZE + " caracteres!")
    private String login;

    @NotBlank(message = "Informe um password!")
    @Size(max = PASSWORD_MAX_SIZE, message = "Informe um password de até " + PASSWORD_MAX_SIZE + " caracteres!")
    private String password;

    @Size(max = AVATAR_MAX_SIZE, message = "Utilize uma imagem de tamanho menor!")
    private String avatar;
}
