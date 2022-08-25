package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IParticipantService {

    ParticipantEntity participantDtoToEntity(ParticipantDto participantDto);
    ParticipantDetailDto participantEntityToParticipantDetailDto(ParticipantEntity participantEntity);
    ResponseEntity<ParticipantDetailDto> newParticipant(ParticipantDto participantDto);

    Optional<ParticipantEntity> findByCode(String code);

    ResponseEntity<PaymentDetailDto> getParticipantReleased(String code);

    ResponseEntity<CashierDto> getParticipantCashier(String code);

    ResponseEntity<PaymentDetailDto> newPayment(PaymentDto paymentDto);
}
