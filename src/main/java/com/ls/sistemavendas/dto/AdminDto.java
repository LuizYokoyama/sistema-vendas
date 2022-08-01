package com.ls.sistemavendas.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdminDto {

    public static final int AVATAR_MAX_SIZE = 3145728;  // 3 Mb
    public static final int NAME_MAX_SIZE = 80;
    public static final int LOGIN_MAX_SIZE = 30;
    public static final int PASSWORD_MAX_SIZE = 15;

    @EqualsAndHashCode.Include
    @NotNull(message = "The name cannot be null")
    @Size(max = NAME_MAX_SIZE, message = "Name size should not be greater then " + NAME_MAX_SIZE)
    private String name;

    @EqualsAndHashCode.Include
    @NotNull(message = "The login cannot be null")
    @Size(max = LOGIN_MAX_SIZE, message = "Login size should not be greater then " + LOGIN_MAX_SIZE)
    private String login;

    @NotNull(message = "The password cannot be null")
    @Size(max = PASSWORD_MAX_SIZE, message = "Password size should not be greater then " + PASSWORD_MAX_SIZE)
    private String password;

    @Size(max = AVATAR_MAX_SIZE, message = "Image size limit exceeded")
    private String avatar;
}
