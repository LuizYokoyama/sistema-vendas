package com.ls.sistemavendas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ls.sistemavendas.dto.FormRegisterDto;
import com.ls.sistemavendas.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;


    @Autowired
    @InjectMocks
    private EventService formService;

    private FormRegisterDto formRegisterDto;

    @BeforeEach
    public void setUp() throws IOException {
        final var json = Paths.get("src", "test", "resources", "input.json");
        formRegisterDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json.toFile(), FormRegisterDto.class);

    }

    @AfterEach
    public void tearDown(){
        formRegisterDto = null;
    }

    @Test
    void givenFormRegisterToAddReturnAddedFormDetails(){

        var eventEntity = formService.formRegisterDtoToEventEntity(formRegisterDto);

        when(eventRepository.save(any())).thenReturn(eventEntity);
        //when(eventRepository.existsByName(any())).thenReturn(true);
        //when(eventRepository.existsByPeriod(any(), anyInt())).thenReturn(true);
        var formRegisterDetailsDto = formService.register(formRegisterDto);

        verify(eventRepository, times(1)).save(any());
        verify(eventRepository, times(1)).existsByName(any());
        verify(eventRepository, times(1)).existsByPeriod(any(), anyInt());
        assertEquals(formRegisterDetailsDto.getBody(), formService.eventEntityToFormDetailsDto(eventEntity));

    }

}
