package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.Entity.TransactionEntity;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.TransactionDto;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public interface IParticipantService {

    ResponseEntity<TransactionDto> newTransaction(@Valid TransactionDto transactionDto);

    public ParticipantEntity participantDtoToEntity(ParticipantDto participantDto);
    public ParticipantDto participantEntityToDto(ParticipantEntity participantEntity);

    public TransactionDto trasactionEntityToDto(TransactionEntity transactionEntity);

    public TransactionEntity transactionDtoToEntity(TransactionDto transactionDto);
}
