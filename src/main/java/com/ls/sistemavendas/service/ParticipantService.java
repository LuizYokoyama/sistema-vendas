package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.exceptions.ParticipantCodeAlreadyUsedRuntimeException;
import com.ls.sistemavendas.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ParticipantService implements IParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<ParticipantDetailDto> newParticipant(ParticipantDto participantDto) {

        if (participantRepository.findById(participantDto.getParticipantCode()).isPresent()){
            throw new ParticipantCodeAlreadyUsedRuntimeException("Use outro código, porque este já foi usado.");
        }
        ParticipantEntity participantEntity = participantDtoToEntity(participantDto);
        participantEntity = participantRepository.save(participantEntity);
        ParticipantDetailDto participantDetailDto = participantEntityToParticipantDetailDto(participantEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(participantDetailDto);
    }

    @Override
    public Optional<ParticipantEntity> findByCode(String code) {
        return participantRepository.findById(code);
    }

    @Override
    public ResponseEntity<ParticipantSummaryDto> getParticipantReleased(String code) {
        return null;
    }

    @Override
    public ResponseEntity<CashierDto> getParticipantCashier(String code) {

        ParticipantSummaryDto participantSummaryDto = participantRepository.getParticipantSummaryById(code);
        List<PurchasedProductsDto> purchasedProductsDtoList = participantRepository.getPurchasedProducts(code);
        double accountTotal = 0;
        for (PurchasedProductsDto purchasedProductsDto: purchasedProductsDtoList){
            accountTotal += purchasedProductsDto.getPrice() * purchasedProductsDto.getQuantity();
        }

        CashierDto cashierDto = new CashierDto(participantSummaryDto, purchasedProductsDtoList, accountTotal);

        return ResponseEntity.ok(cashierDto);
    }

    @Override
    public ParticipantDetailDto participantEntityToParticipantDetailDto(ParticipantEntity participantEntity) {

        ParticipantDetailDto participantDetailDto = new ParticipantDetailDto();

        participantDetailDto.setParticipantCode(participantEntity.getCode());
        participantDetailDto.setName(participantEntity.getName());
        participantDetailDto.setPassword(participantEntity.getPassword());
        participantDetailDto.setEntryDateTime(participantEntity.getEntryDateTime());

        return participantDetailDto;

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
