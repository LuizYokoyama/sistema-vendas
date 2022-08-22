package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.ParticipantDto;
import org.springframework.http.ResponseEntity;

public interface IParticipantService {

    public ParticipantEntity participantDtoToEntity(ParticipantDto participantDto);
    public ParticipantDto participantEntityToDto(ParticipantEntity participantEntity);
    ResponseEntity<ParticipantDto> newParticipant(ParticipantDto participantDto);
}
