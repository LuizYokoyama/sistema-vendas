package com.ls.sistemavendas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ls.sistemavendas.Entity.EventAgentEntity;
import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.Entity.StandAgentEntity;
import com.ls.sistemavendas.dto.FormDetailsDto;
import com.ls.sistemavendas.dto.FormRegisterDto;
import com.ls.sistemavendas.repository.EventAgentRepository;
import com.ls.sistemavendas.repository.EventRepository;
import com.ls.sistemavendas.repository.StandAgentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private StandAgentRepository standAgentRepository;

    @MockBean
    private EventAgentRepository eventAgentRepository;

    @MockBean
    private KeycloakService keycloakService;

    @Autowired
    @InjectMocks
    private EventService eventService;

    private FormRegisterDto formRegisterDto;

    private FormDetailsDto formDetailsDto;

    private EventEntity eventEntity;
    private Optional<EventEntity> eventEntityOptional;

    private ResponseEntity<String> keycloakResponse;

    private List<UserRepresentation> keycloakUsers;

    @BeforeEach
    public void setUp() throws IOException {
        var pathFormReg = Paths.get("src", "test", "resources", "formRegister.json");
        formRegisterDto = new ObjectMapper().registerModule(
                new JavaTimeModule()).readValue(pathFormReg.toFile(), FormRegisterDto.class);

        var pathFormDetails = Paths.get("src", "test", "resources", "formDetails.json");
        formDetailsDto = new ObjectMapper().registerModule(
                new JavaTimeModule()).readValue(pathFormDetails.toFile(), FormDetailsDto.class);

        eventEntity = eventService.formRegisterDtoToEventEntity(formRegisterDto);

        eventEntityOptional = Optional.of(eventEntity);

        keycloakResponse = ResponseEntity.status(HttpStatus.CREATED).body("");
        keycloakUsers = new ArrayList<>();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("test");
        keycloakUsers.add(userRepresentation);
    }

    @AfterEach
    public void tearDown(){
        formRegisterDto = null;
        eventEntityOptional = null;
        keycloakResponse = null;
        keycloakUsers = null;
    }

    @Test
    void givenFormRegisterToAddReturnAddedFormDetails(){

        when(eventRepository.save(any())).thenReturn(eventEntity);
        when(keycloakService.addUserAdmin(any())).thenReturn(keycloakResponse);
        when(keycloakService.getUser(any())).thenReturn(keycloakUsers);
        var responseFormDetailsDto = eventService.register(formRegisterDto);

        verify(eventRepository, times(1)).save(any());
        verify(eventRepository, times(1)).existsByName(any());
        verify(eventRepository, times(1)).existsByPeriod(any(), anyInt());
        verify(keycloakService, times(1)).addUserAdmin(any());
        verify(keycloakService, times(1)).getUser(any());
        assertEquals(responseFormDetailsDto.getBody(), eventService.eventEntityToFormDetailsDto(eventEntity));

    }

    @Test
    void givenFormDetailsToPutReturnUpdatedFormDetails(){

        when(eventRepository.save(any())).thenReturn(eventEntity);
        when(eventRepository.findById(any())).thenReturn(eventEntityOptional);

        var responseFormDetailsDto = eventService.update(formDetailsDto);

        verify(eventRepository, times(1)).save(any());
        verify(eventRepository, times(1)).findById(any());
        verify(keycloakService, times(1)).updateUserAdmin(any());
        assertEquals(responseFormDetailsDto.getBody(), eventService.eventEntityToFormDetailsDto(eventEntity));

    }

    @Test
    void givenEventIdReturnFormDetails(){

        when(eventRepository.findById(any())).thenReturn(eventEntityOptional);

        var responseFormDetailsDto = eventService.getEvent(any());

        verify(eventRepository, times(1)).findById(any());
        assertEquals(responseFormDetailsDto.getBody(), eventService.eventEntityToFormDetailsDto(eventEntity));

    }

    @Test
    void givenNewStandAgentRequestReturnNewAddedStandAgentDto(){

        when(keycloakService.addUserStandAgent(any())).thenReturn(keycloakResponse);
        when(keycloakService.getUser(any())).thenReturn(keycloakUsers);
        when(standAgentRepository.save(any())).thenReturn(new StandAgentEntity());

        var responseStandAgentDto = eventService.newStandAgent();

        verify(keycloakService, times(1)).addUserStandAgent(any());
        verify(keycloakService, times(1)).getUser(any());
        verify(standAgentRepository, times(1)).save(any());
        assertEquals(responseStandAgentDto.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    void givenNewEventAgentRequestReturnNewAddedEventAgentDto(){

        when(keycloakService.addUserEventAgent(any())).thenReturn(keycloakResponse);
        when(keycloakService.getUser(any())).thenReturn(keycloakUsers);
        when(eventAgentRepository.save(any())).thenReturn(new EventAgentEntity());

        var responseEventAgentDto = eventService.newEventAgent();

        verify(keycloakService, times(1)).addUserEventAgent(any());
        verify(keycloakService, times(1)).getUser(any());
        verify(eventAgentRepository, times(1)).save(any());
        assertEquals(responseEventAgentDto.getStatusCode(), HttpStatus.CREATED);

    }

}
