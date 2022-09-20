package com.ls.sistemavendas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.Entity.StandAgentEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.Entity.TransactionItemEntity;
import com.ls.sistemavendas.dto.StandAgentDto;
import com.ls.sistemavendas.dto.TransactionDto;
import com.ls.sistemavendas.dto.TransactionResponseDto;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.StandAgentRepository;
import com.ls.sistemavendas.repository.StandRepository;
import com.ls.sistemavendas.repository.TransactionItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StandServiceTest {

    @MockBean
    private StandRepository standRepository;

    @MockBean
    private StandAgentRepository standAgentRepository;

    @MockBean
    private ParticipantRepository participantRepository;

    @MockBean
    private TransactionItemRepository transactionItemRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private StandService standService;

    private Optional<StandEntity> standEntityOptional;
    private StandEntity standEntity;
    private Optional<ParticipantEntity> participantEntityOptional;
    private ParticipantEntity participantEntity;
    private TransactionItemEntity transactionItemEntity;

    private TransactionDto transactionDto;
    private TransactionResponseDto transactionResponseDto;
    private Optional<StandAgentEntity> standAgentEntityOptional;
    private StandAgentEntity standAgentEntity;
    private StandAgentDto standAgentDto;


    @BeforeEach
    public void setUp() throws IOException {

        var pathtransactionDto = Paths.get("src", "test", "resources", "transactionDto.json");
        transactionDto = new ObjectMapper().readValue(pathtransactionDto.toFile(), TransactionDto.class);
        transactionResponseDto = new TransactionResponseDto();
        BeanUtils.copyProperties(transactionDto, transactionResponseDto);
        standEntity = new StandEntity();
        standEntity.setProductsList(new HashSet<>());
        standEntityOptional = Optional.of(standEntity);
        participantEntity = new ParticipantEntity();
        participantEntityOptional = Optional.of(participantEntity);
        transactionItemEntity = new TransactionItemEntity();
        standAgentEntity = new StandAgentEntity();
        standAgentEntityOptional = Optional.of(standAgentEntity);
        standAgentDto = new StandAgentDto();

    }

    @AfterEach
    public void tearDown(){

        standEntity = null;
        standEntityOptional = null;
        participantEntity = null;
        participantEntityOptional = null;
        transactionItemEntity = null;
        transactionDto = null;
        transactionResponseDto = null;
        standAgentEntity = null;
        standAgentEntityOptional = null;
        standAgentDto = null;

    }

    @Test
    void givenStandIdReturnStandDetails(){

        when(standRepository.findById(any())).thenReturn(standEntityOptional);

        var responseStandDetailsDto = standService.getStandDetails(any());

        verify(standRepository, times(1)).findById(any());
        assertEquals(responseStandDetailsDto.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void givenTransactionDtoToAddReturnAddedTransactionResponseDto(){

        when(participantRepository.findById(any())).thenReturn(participantEntityOptional);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(standRepository.findById(any())).thenReturn(standEntityOptional);
        when(standRepository.save(any())).thenReturn(standEntity);
        when(transactionItemRepository.save(any())).thenReturn(transactionItemEntity);

        var responseEntityTransactionResponseDto = standService.newTransaction(transactionDto);

        verify(participantRepository, times(1)).findById(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
        verify(standRepository, times(1)).findById(any());
        verify(standRepository, times(1)).save(any());
        verify(transactionItemRepository, times(1)).save(any());
        assertEquals(responseEntityTransactionResponseDto.getBody(), transactionResponseDto);
        assertEquals(responseEntityTransactionResponseDto.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    void givenStandAgentCodeAndNameToPutReturnStandAgentDto(){

        when(standAgentRepository.findById(any())).thenReturn(standAgentEntityOptional);
        when(standAgentRepository.save(any())).thenReturn(standAgentEntity);

        var responseStandDetailsDto = standService.setStandAgentName(null, null);

        verify(standAgentRepository, times(1)).findById(any());
        verify(standAgentRepository, times(1)).save(any());
        assertEquals(responseStandDetailsDto.getStatusCode(), HttpStatus.OK);
        assertEquals(responseStandDetailsDto.getBody(), standAgentDto);
    }

}
