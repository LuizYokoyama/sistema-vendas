package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.dto.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public interface IStandService {

    ResponseEntity<StandDetailDto> getStandDetails(UUID id);
    Optional<StandEntity> findById(UUID id);

    ResponseEntity<TransactionResponseDto> newTransaction(@Valid TransactionDto transactionDto);

    ResponseEntity<StandAgentDto> setStandAgentName(String code, String name);
}
