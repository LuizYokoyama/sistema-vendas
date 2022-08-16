package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.Entity.TransactionItemEntity;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.TransactionDto;
import com.ls.sistemavendas.dto.TransactionItemDto;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.TransactionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class ParticipantService implements IParticipantService {

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    @Transactional
    public ResponseEntity<TransactionDto> newTransaction(@Valid TransactionDto transactionDto) {

        for (TransactionItemDto transactionItemDto : transactionDto.getItems()){
            TransactionItemEntity transactionItemEntity = new TransactionItemEntity();
            transactionItemEntity.setParticipantCode(transactionDto.getParticipantCode());
            transactionItemEntity.setProduct(transactionItemDto.getProductID());
            transactionItemEntity.setDateTime(transactionItemDto.getDateTime());
            transactionItemEntity.setQuantity(transactionItemDto.getQuantity());
            transactionItemEntity = transactionItemRepository.save(transactionItemEntity);
            transactionItemDto.setId(transactionItemEntity.getId());
            transactionItemDto.setProductID(transactionItemEntity.getProduct());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDto);
    }

    @Override
    @Transactional
    public ResponseEntity<ParticipantDto> newParticipant(ParticipantDto participantDto) {

        ParticipantEntity participantEntity = participantDtoToEntity(participantDto);
        participantEntity = participantRepository.save(participantEntity);
        participantDto = participantEntityToDto(participantEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(participantDto);
    }

    @Override
    public ParticipantDto participantEntityToDto(ParticipantEntity participantEntity) {

        ParticipantDto participantDto = new ParticipantDto();

        participantDto.setParticipantCode(participantEntity.getCode());
        participantDto.setName(participantEntity.getName());
        participantDto.setPassword(participantEntity.getPassword());

        return participantDto;

    }

    @Override

    public ParticipantEntity participantDtoToEntity(ParticipantDto participantDto) {

        ParticipantEntity participantEntity = new ParticipantEntity();

        participantEntity.setCode(participantDto.getParticipantCode());
        participantEntity.setName(participantDto.getName());
        participantEntity.setPassword(participantDto.getPassword());

        return participantEntity;

    }

}
