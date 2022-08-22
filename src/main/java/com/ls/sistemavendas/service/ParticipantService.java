package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.exceptions.ParticipantCodeAlreadyUsedRuntimeException;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.TransactionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ParticipantService implements IParticipantService {

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<ParticipantDto> newParticipant(ParticipantDto participantDto) {

        if (participantRepository.findById(participantDto.getParticipantCode()).isPresent()){
            throw new ParticipantCodeAlreadyUsedRuntimeException("Use outro código, porque este já foi usado.");
        }
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
        participantEntity.setPassword(passwordEncoder.encode(participantDto.getPassword()) );

        return participantEntity;

    }

}
