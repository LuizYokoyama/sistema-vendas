package com.ls.sistemavendas.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdminKeycloakResponseDto {

    @EqualsAndHashCode.Include
    String access_token;
    int expires_in;
    int refresh_expires_in;
    String refresh_token;
    String token_type;
    int not_before_policy;
    String session_state;
    String scope;
    UUID eventId;

}
