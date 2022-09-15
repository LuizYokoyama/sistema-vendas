package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventService {

    ResponseEntity<FormDetailsDto> register(@Valid FormRegisterDto formRegisterDto);

    ResponseEntity<FormDetailsDto> update(@Valid FormDetailsDto formDetailsDto);

    List findAllFull();


    Optional<EventEntity> findById(UUID id);

    FormRegisterDto eventEntityToFormRegDto(EventEntity eventEntity);

    FormDetailsDto eventEntityToFormDetailsDto(EventEntity eventEntity);

    EventEntity formRegisterDtoToEventEntity(FormRegisterDto formRegisterDto);

    EventEntity formDetailsDtoToEventEntity(FormDetailsDto formDetailsDto);


    ResponseEntity<FormDetailsDto> getEvent(UUID id);

    ResponseEntity<StandAgentDto> newStandAgent();

    ResponseEntity<EventAgentDto> newEventAgent();

    ResponseEntity<agentKeycloakResponseDto> agentLogin(String username);

    ResponseEntity<EventAgentDto> setEventAgentName(String code, String name);
}
