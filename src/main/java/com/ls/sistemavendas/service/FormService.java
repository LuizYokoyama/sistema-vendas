package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.Entity.ProductEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;


@Service
@Validated
public class FormService implements IFormService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional
    public ResponseEntity<FormDto> save(@Valid FormDto formDto) {

        EventEntity eventEntity = formDtoToEventEntity(formDto);
        eventEntity = eventRepository.save(eventEntity);
        formDto = eventEntityToFormDto(eventEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(formDto);
    }

    @Override
    public List findAllFull() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<EventEntity> findById(UUID id) {
        return eventRepository.findById(id);
    }

    @Override
    public FormDto eventEntityToFormDto(EventEntity eventEntity) {

        FormDto formDto = new FormDto();
        Set<StandDto> standDtos = new HashSet<>();

        for (StandEntity standEntity : eventEntity.getStandsList() ){
            StandDto standDto = new StandDto();
            Set<ProductDto> productDtos = new HashSet<>();
            for (ProductEntity productEntity : standEntity.getProductsList()){
                ProductDto productDto = new ProductDto(productEntity.getId(), productEntity.getDescription(),
                        productEntity.getPrice());
                productDtos.add(productDto);
            }
            standDto.setProductsList(productDtos);
            standDto.setDescription(standEntity.getDescription());
            standDto.setId(standEntity.getId());
            standDto.setIndex(standEntity.getIndex());
            standDto.setTotalAgents(standEntity.getTotalAgents());
            standDtos.add(standDto);
        }
        System.out.println(standDtos.toString());
        formDto.setStandsList(standDtos);
        formDto.setEvent(new EventDto(eventEntity.getId(), eventEntity.getName(), eventEntity.getPhoto(),
                eventEntity.getDescription(), eventEntity.getTotalAgents(), eventEntity.getFirstOccurrenceDateTime(),
                eventEntity.getDuration()));
        formDto.setAdmin(new AdminDto(eventEntity.getAdminName(), eventEntity.getLogin(), eventEntity.getPassword(),
                eventEntity.getAvatar()));

        return formDto;

    }

    @Override
    public EventEntity formDtoToEventEntity(FormDto formDto) {

        EventEntity eventEntity = new EventEntity();

        Set<StandDto> standsDto;
        Set<StandEntity> standsEntity = new HashSet<>();
        standsDto = formDto.getStandsList();
        for (StandDto standDto : standsDto){
            StandEntity standEntity = new StandEntity();
            standEntity.setIndex(standDto.getIndex());
            standEntity.setId(standDto.getId());
            standEntity.setEvent(eventEntity);
            standEntity.setDescription(standDto.getDescription());
            standEntity.setTotalAgents(standDto.getTotalAgents());
            Set<ProductEntity> productEntities = new HashSet<>();
            for (ProductDto productDto : standDto.getProductsList()){
                ProductEntity productEntity = new ProductEntity();
                productEntity.setId(productDto.getId());
                productEntity.setDescription(productDto.getDescription());
                productEntity.setPrice(productDto.getPrice());
                productEntity.setStand(standEntity);
                productEntities.add(productEntity);
            }
            standEntity.setProductsList(productEntities);
            standsEntity.add(standEntity);
        }
        eventEntity.setStandsList(standsEntity);
        eventEntity.setName(formDto.getEvent().getEventName());
        eventEntity.setPhoto(formDto.getEvent().getPhoto());
        eventEntity.setTotalAgents(formDto.getEvent().getTotalAgents());
        eventEntity.setAdminName(formDto.getAdmin().getName());
        eventEntity.setAvatar(formDto.getAdmin().getAvatar());
        eventEntity.setLogin(formDto.getAdmin().getLogin());
        eventEntity.setPassword(formDto.getAdmin().getPassword());
        eventEntity.setPhoto(formDto.getEvent().getPhoto());
        eventEntity.setDuration(formDto.getEvent().getDuration());
        eventEntity.setDescription(formDto.getEvent().getDescription());
        eventEntity.setFirstOccurrenceDateTime(formDto.getEvent().getFirstOccurrenceDateTime());
        eventEntity.setId(formDto.getEvent().getId());

        return eventEntity;
    }

}
