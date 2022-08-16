package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.Entity.ProductEntity;
import com.ls.sistemavendas.Entity.TransactionEntity;
import com.ls.sistemavendas.Entity.TransactionItemEntity;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.TransactionDto;
import com.ls.sistemavendas.dto.TransactionItemDto;
import com.ls.sistemavendas.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
@Validated
public class ParticipantService implements IParticipantService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public ResponseEntity<TransactionDto> newTransaction(@Valid TransactionDto transactionDto) {

        TransactionEntity transactionEntity = transactionDtoToEntity(transactionDto);
        transactionEntity = transactionRepository.save(transactionEntity);
        transactionDto = trasactionEntityToDto(transactionEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDto);
    }

    @Override
    public ParticipantDto participantEntityToDto(ParticipantEntity participantEntity) {

        ParticipantDto participantDto = new ParticipantDto();

        participantDto.setId(participantEntity.getId());
        participantDto.setCode(participantEntity.getCode());
        participantDto.setName(participantEntity.getName());
        participantDto.setPassword(participantEntity.getPassword());

        return participantDto;

    }

    @Override
    public TransactionDto trasactionEntityToDto(TransactionEntity transactionEntity) {

        TransactionDto transactionDto = new TransactionDto();
        Set<TransactionItemDto> items = new HashSet<>();

        for (TransactionItemEntity itemEntity : transactionEntity.getItems() ){
            TransactionItemDto itemDto = new TransactionItemDto();
            itemDto.setDateTime(itemEntity.getDateTime());
            itemDto.setProductId(itemEntity.getProduct().getId());
            items.add(itemDto);
        }
        transactionDto.setItems(items);
       // transactionDto.setParticipantDto(participantEntityToDto(transactionEntity.getParticipant()));

        return transactionDto;

    }

    public ParticipantEntity participantDtoToEntity(ParticipantDto participantDto) {

        ParticipantEntity participantEntity = new ParticipantEntity();

        participantEntity.setId(participantDto.getId());
        participantEntity.setCode(participantDto.getCode());
        participantEntity.setName(participantDto.getName());
        participantEntity.setPassword(participantDto.getPassword());

        return participantEntity;

    }

    public TransactionEntity transactionDtoToEntity(TransactionDto transactionDto) {

        TransactionEntity transactionEntity = new TransactionEntity();
        Set<TransactionItemEntity> items = new HashSet<>();

        for (TransactionItemDto itemDto : transactionDto.getItems() ){
            TransactionItemEntity itemEntity = new TransactionItemEntity();

            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(itemDto.getProductId());

            itemEntity.setProduct(productEntity);
            itemEntity.setId(itemDto.getId());

            itemEntity.setDateTime(itemDto.getDateTime());
            itemEntity.setQuantity(itemDto.getQuantity());
            items.add(itemEntity);
        }
        transactionEntity.setItems(items);
       // transactionEntity.setParticipant(participantDtoToEntity(transactionDto.getParticipantDto()));

        return transactionEntity;

    }


}
