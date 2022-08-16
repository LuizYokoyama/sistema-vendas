package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.TransactionDto;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public interface IParticipantService {

    ResponseEntity<TransactionDto> newTransaction(@Valid TransactionDto transactionDto);

    public ParticipantEntity participantDtoToEntity(ParticipantDto participantDto);
    public ParticipantDto participantEntityToDto(ParticipantEntity participantEntity);
    ResponseEntity<ParticipantDto> newParticipant(ParticipantDto participantDto);
}
