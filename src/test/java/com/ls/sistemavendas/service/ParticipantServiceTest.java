package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.ParticipantEntity;
import com.ls.sistemavendas.Entity.PaymentEntity;
import com.ls.sistemavendas.dto.CashierDto;
import com.ls.sistemavendas.dto.ParticipantDetailDto;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.ParticipantReleasedDto;
import com.ls.sistemavendas.repository.ParticipantRepository;
import com.ls.sistemavendas.repository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

    @MockBean
    private ParticipantRepository participantRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    @InjectMocks
    private ParticipantService participantService;

    private ParticipantDto participantDto;
    private ParticipantEntity participantEntity;
    private ParticipantDetailDto participantDetailDto;
    private PaymentEntity paymentEntity;
    private ParticipantReleasedDto participantReleasedDto;

    @BeforeEach
    public void setUp(){

        participantEntity = new ParticipantEntity("testCode", "testName", "pswd", new Timestamp(32131), new ArrayList<>());
        participantDto = new ParticipantDto("testCode", "testName", "pswd", new HashSet<>());
        participantDetailDto = participantService.participantEntityToParticipantDetailDto(participantEntity);
        paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentItems(new HashSet<>());
        participantReleasedDto = new ParticipantReleasedDto();


    }

    @AfterEach
    public void tearDown(){

        participantEntity = null;
        participantDto = null;
        participantDetailDto = null;
        paymentEntity = null;
        participantReleasedDto = null;


    }

    @Test
    void givenParticipantDtoToAddReturnAddedParticipantDetails(){

        when(participantRepository.save(any())).thenReturn(participantEntity);
        when(participantRepository.existsByCode(any())).thenReturn(false);

        var responseParticipantDetailsDto = participantService.newParticipant(participantDto);

        verify(participantRepository, times(1)).save(any());
        verify(participantRepository, times(1)).existsByCode(any());
        assertEquals(responseParticipantDetailsDto.getBody(), participantDetailDto);
    }

    @Test
    void givenParticipantCodeReturnParticipantReleasedDto(){

        when(paymentRepository.findByParticipantCode(any())).thenReturn(paymentEntity);
        when(participantRepository.existsByCode(any())).thenReturn(true);

        ResponseEntity<ParticipantReleasedDto> responseEntity = participantService.getParticipantReleased(any());

        verify(participantRepository, times(1)).existsByCode(any());
        verify(paymentRepository, times(1)).findByParticipantCode(any());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void givenParticipantCodeReturnAParticipantCashierDto(){

        when(participantRepository.existsByCode(any())).thenReturn(true);

        ResponseEntity<CashierDto> responseEntity = participantService.getParticipantCashier(any());

        verify(participantRepository, times(1)).existsByCode(any());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }

}
