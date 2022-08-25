package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.Entity.PaymentEntity;
import com.ls.sistemavendas.Entity.PaymentItemEntity;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.exceptions.ParticipantCodeAlreadyUsedRuntimeException;
import com.ls.sistemavendas.exceptions.ParticipantCodeNotFoundRuntimeException;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
public class ParticipantService implements IParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PaymentRepository paymentRepository;

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
    public ResponseEntity<PaymentDetailDto> getParticipantReleased(String code) {

        if (!participantRepository.findById(code).isPresent()){
            throw new ParticipantCodeNotFoundRuntimeException("Confira o código do participante," +
                    " porque este não está cadastrado!");
        }

        PaymentEntity paymentEntity = paymentRepository.findByParticipantCode(code);
        if (paymentEntity == null){
            throw new ParticipantCodeNotFoundRuntimeException("Não constam pagamentos para o código de participante!");
        }
        PaymentDetailDto paymentDetailDto = paymentEntityToPaymentDetailDto(paymentEntity);

        return ResponseEntity.ok(paymentDetailDto);
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
    public ResponseEntity<PaymentDetailDto> newPayment(PaymentDto paymentDto) {
        if (!participantRepository.findById(paymentDto.getParticipantCode()).isPresent()){
            throw new ParticipantCodeNotFoundRuntimeException("Confira se o código do participante está correto," +
                    " porque este código informado não está cadastrado no sistema.");
        }
        PaymentEntity paymentEntity = paymentDtoToPaymentEntity(paymentDto);
        paymentEntity = paymentRepository.save(paymentEntity);
        PaymentDetailDto paymentDetailDto = paymentEntityToPaymentDetailDto(paymentEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentDetailDto);
    }

    private PaymentDetailDto paymentEntityToPaymentDetailDto(PaymentEntity paymentEntity) {

        PaymentDetailDto paymentDetailDto = new PaymentDetailDto();

        paymentDetailDto.setTotalPayment(paymentEntity.getTotalPayment());
        paymentDetailDto.setParticipantCode(paymentEntity.getParticipantCode());
        paymentDetailDto.setComment(paymentEntity.getComment());
        paymentDetailDto.setId(paymentEntity.getId());

        Set<PaymentItemDetailDto> paymentItemDetailDtoSet = new HashSet<>();
        for (PaymentItemEntity paymentItemEntity : paymentEntity.getPaymentItems()){

            PaymentItemDetailDto paymentItemDetailDto = new PaymentItemDetailDto();

            paymentItemDetailDto.setId(paymentItemEntity.getId());
            paymentItemDetailDto.setNote(paymentItemEntity.getNote());
            paymentItemDetailDto.setType(paymentItemEntity.getType());
            paymentItemDetailDto.setValue(paymentItemEntity.getValue());
            paymentItemDetailDtoSet.add(paymentItemDetailDto);
        }
        paymentDetailDto.setPaymentItems(paymentItemDetailDtoSet);

        return  paymentDetailDto;
    }

    private PaymentEntity paymentDtoToPaymentEntity(PaymentDto paymentDto) {

        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setTotalPayment(paymentDto.getTotalPayment());
        paymentEntity.setParticipantCode(paymentDto.getParticipantCode());
        paymentEntity.setComment(paymentDto.getComment());
        paymentEntity.setId(paymentDto.getId());

        Set<PaymentItemEntity> paymentItemEntitySet = new HashSet<>();
        for (PaymentItemDto paymentItemDto : paymentDto.getPaymentItems()){
            PaymentItemEntity paymentItemEntity = new PaymentItemEntity();
            paymentItemEntity.setPaymentEntity(paymentEntity);
            paymentItemEntity.setId(paymentItemDto.getId());
            paymentItemEntity.setNote(paymentItemDto.getNote());
            paymentItemEntity.setType(paymentItemDto.getType());
            paymentItemEntity.setValue(paymentItemDto.getValue());

            paymentItemEntitySet.add(paymentItemEntity);
        }
        paymentEntity.setPaymentItems(paymentItemEntitySet);

        return paymentEntity;
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
