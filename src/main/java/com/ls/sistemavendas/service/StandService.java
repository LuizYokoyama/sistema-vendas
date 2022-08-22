package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ProductEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.Entity.TransactionItemEntity;
import com.ls.sistemavendas.dto.ProductDetailDto;
import com.ls.sistemavendas.dto.StandDetailDto;
import com.ls.sistemavendas.dto.TransactionDto;
import com.ls.sistemavendas.dto.TransactionItemDto;
import com.ls.sistemavendas.exceptions.BadCredentialsRuntimeException;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.StandRepository;
import com.ls.sistemavendas.repository.TransactionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Validated
public class StandService implements IStandService{

    @Autowired
    private StandRepository standRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<StandEntity> findById(UUID id) {
        return standRepository.findById(id);
    }

    @Override
    public ResponseEntity<StandDetailDto> getStandDetails(UUID id) {

        StandEntity standEntity = standRepository.findById(id).get();

        StandDetailDto standDetailDto = new StandDetailDto();
        Set<ProductDetailDto> productDtos = new HashSet<>();
        for (ProductEntity productEntity : standEntity.getProductsList()){
            ProductDetailDto productDto = new ProductDetailDto(productEntity.getId(), productEntity.getDescription(),
                    productEntity.getPrice());
            productDtos.add(productDto);
        }
        standDetailDto.setProductsList(productDtos);
        standDetailDto.setDescription(standEntity.getDescription());
        standDetailDto.setId(standEntity.getId());
        standDetailDto.setIndex(standEntity.getIndex());
        standDetailDto.setStandTotalAgents(standEntity.getTotalAgents());

        return ResponseEntity.ok(standDetailDto);
    }

    @Override
    @Transactional
    public ResponseEntity<TransactionDto> newTransaction(@Valid TransactionDto transactionDto) {

        if (!passwordEncoder.matches(transactionDto.getPassword(),
                participantRepository.findById(transactionDto.getParticipantCode()).get().getPassword())){
            throw new BadCredentialsRuntimeException("Código do participante ou senha inválidos!");
        }
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
}
