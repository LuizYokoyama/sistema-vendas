package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.dto.StandDetailDto;
import com.ls.sistemavendas.dto.TransactionDto;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public interface IStandService {

    public ResponseEntity<StandDetailDto> getStandDetails(UUID id);
    public Optional<StandEntity> findById(UUID id);

    public ResponseEntity<TransactionDto> newTransaction(@Valid TransactionDto transactionDto);

    
}
