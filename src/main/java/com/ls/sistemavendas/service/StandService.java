package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ProductEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.Entity.TransactionItemEntity;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.exceptions.BadCredentialsRuntimeException;
import com.ls.sistemavendas.exceptions.StandNotFoundRuntimeException;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.StandRepository;
import com.ls.sistemavendas.repository.TransactionItemRepository;
import org.springframework.beans.BeanUtils;
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
        standDetailDto.setTotalTransactions(standEntity.getTotalTransactions());
        standDetailDto.setStandTotalAgents(standEntity.getTotalAgents());

        return ResponseEntity.ok(standDetailDto);
    }

    @Override
    @Transactional
    public ResponseEntity<TransactionResponseDto> newTransaction(@Valid TransactionDto transactionDto) {

        if (!passwordEncoder.matches(transactionDto.getPassword(),
                participantRepository.findById(transactionDto.getParticipantCode()).get().getPassword())){
            throw new BadCredentialsRuntimeException("Código do participante ou senha inválidos!");
        }
        Optional<StandEntity> standEntityOptional= standRepository.findById(transactionDto.getStandId());
        if (!standEntityOptional.isPresent()){
            throw new StandNotFoundRuntimeException("Verifique o standId, porque este standId não foi encontrado!");
        }
        StandEntity standEntity = standEntityOptional.get();
        standEntity.setTotalTransactions(standEntity.getTotalTransactions() + transactionDto.getTotalTransaction());
        standEntity = standRepository.save(standEntity);


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

        TransactionResponseDto transactionResponseDto= new TransactionResponseDto();
        BeanUtils.copyProperties(transactionDto, transactionResponseDto);
        transactionResponseDto.setStandTotalTransactions(standEntity.getTotalTransactions());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDto);
    }
}
