package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.FormDto;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFormService {

    ResponseEntity<FormDto> save(@Valid FormDto formDto);
    List findAllFull();


    Optional<EventEntity> findById(UUID id);

    FormDto eventEntityToFormDto(EventEntity eventEntity);

    EventEntity formDtoToEventEntity(FormDto formDto);


}
