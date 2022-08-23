package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.CashierDto;
import com.ls.sistemavendas.dto.ParticipantDetailDto;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.ParticipantSummaryDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IParticipantService {

    public ParticipantEntity participantDtoToEntity(ParticipantDto participantDto);
    public ParticipantDetailDto participantEntityToParticipantDetailDto(ParticipantEntity participantEntity);
    ResponseEntity<ParticipantDetailDto> newParticipant(ParticipantDto participantDto);

    Optional<ParticipantEntity> findByCode(String code);

    ResponseEntity<ParticipantSummaryDto> getParticipantReleased(String code);

    ResponseEntity<CashierDto> getParticipantCashier(String code);
}
