package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.*;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.repository.EventAgentRepository;
import com.ls.sistemavendas.repository.EventRepository;
import com.ls.sistemavendas.repository.StandAgentRepository;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private EventAgentRepository eventAgentRepository;

    @Autowired
    private StandAgentRepository standAgentRepository;

    @Override
    @Transactional
    public ResponseEntity<FormDetailsDto> register(@Valid FormRegisterDto formRegisterDto) {

        EventEntity eventEntity = formRegisterDtoToEventEntity(formRegisterDto);
        eventEntity = eventRepository.save(eventEntity);
        FormDetailsDto formDetailsDto = eventEntityToFormDetailsDto(eventEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(formDetailsDto);
    }

    @Override
    @Transactional
    public ResponseEntity<FormDetailsDto> update(FormDetailsDto formDetailsDto) {
        EventEntity eventEntity = formDetailsDtoToEventEntity(formDetailsDto);
        eventEntity = eventRepository.save(eventEntity);
        formDetailsDto = eventEntityToFormDetailsDto(eventEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(formDetailsDto);
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
    public FormRegisterDto eventEntityToFormRegDto(EventEntity eventEntity) {

        FormRegisterDto formRegisterDto = new FormRegisterDto();
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
        formRegisterDto.setStandsList(standDtos);
        formRegisterDto.setEvent(new EventDto(eventEntity.getId(), eventEntity.getName(), eventEntity.getPhoto(),
                eventEntity.getDescription(), eventEntity.getTotalAgents(), eventEntity.getFirstOccurrenceDateTime(),
                eventEntity.getDuration()));
        formRegisterDto.setAdmin(new AdminDto(eventEntity.getAdminName(), eventEntity.getLogin(), eventEntity.getPassword(),
                eventEntity.getAvatar()));

        return formRegisterDto;

    }

    @Override
    public FormDetailsDto eventEntityToFormDetailsDto(EventEntity eventEntity) {
        FormDetailsDto formDetailsDto = new FormDetailsDto();
        Set<StandDetailDto> standDtos = new HashSet<>();

        for (StandEntity standEntity : eventEntity.getStandsList() ){
            StandDetailDto standDto = new StandDetailDto();
            Set<ProductDetailDto> productDtos = new HashSet<>();
            for (ProductEntity productEntity : standEntity.getProductsList()){
                ProductDetailDto productDto = new ProductDetailDto(productEntity.getId(),
                        productEntity.getDescription(), productEntity.getPrice());
                productDtos.add(productDto);
            }
            standDto.setProductsList(productDtos);
            standDto.setDescription(standEntity.getDescription());
            standDto.setId(standEntity.getId());
            standDto.setIndex(standEntity.getIndex());
            standDto.setTotalAgents(standEntity.getTotalAgents());
            Set<StandAgentDto> standAgentDtos = new HashSet<>();
            if (standEntity.getAgentsList() != null) {
                for (StandAgentEntity standAgentEntity : standEntity.getAgentsList()) {
                    StandAgentDto standAgentDto = new StandAgentDto(standAgentEntity.getId(), standAgentEntity.getName());
                    standAgentDtos.add(standAgentDto);
                }
                standDto.setAgentsList(standAgentDtos);
            }
            standDtos.add(standDto);
        }
        formDetailsDto.setStandsList(standDtos);
        formDetailsDto.setEvent(new EventDetailDto(eventEntity.getId(), eventEntity.getName(), eventEntity.getPhoto(),
                eventEntity.getDescription(), eventEntity.getTotalAgents(), eventEntity.getFirstOccurrenceDateTime(),
                eventEntity.getDuration()));
        formDetailsDto.setAdmin(new AdminDto(eventEntity.getAdminName(), eventEntity.getLogin(),
                eventEntity.getPassword(), eventEntity.getAvatar()));
        Set<EventAgentDto> agentDtos = new HashSet<>();
        if (eventEntity.getAgentsList() != null){
            for (EventAgentEntity agentEntity : eventEntity.getAgentsList()) {
                EventAgentDto agentDto = new EventAgentDto(agentEntity.getId(), agentEntity.getName());
                agentDtos.add(agentDto);
            }
            formDetailsDto.setAgentsList(agentDtos);
        }

        return formDetailsDto;
    }

    @Override
    public EventEntity formRegisterDtoToEventEntity(FormRegisterDto formRegisterDto) {

        EventEntity eventEntity = new EventEntity();

        Set<StandDto> standsDto;
        Set<StandEntity> standsEntity = new HashSet<>();
        standsDto = formRegisterDto.getStandsList();
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
        eventEntity.setName(formRegisterDto.getEvent().getEventName());
        eventEntity.setPhoto(formRegisterDto.getEvent().getPhoto());
        eventEntity.setTotalAgents(formRegisterDto.getEvent().getTotalAgents());
        eventEntity.setAdminName(formRegisterDto.getAdmin().getName());
        eventEntity.setAvatar(formRegisterDto.getAdmin().getAvatar());
        eventEntity.setLogin(formRegisterDto.getAdmin().getLogin());
        eventEntity.setPassword(formRegisterDto.getAdmin().getPassword());
        eventEntity.setPhoto(formRegisterDto.getEvent().getPhoto());
        eventEntity.setDuration(formRegisterDto.getEvent().getDuration());
        eventEntity.setDescription(formRegisterDto.getEvent().getDescription());
        eventEntity.setFirstOccurrenceDateTime(formRegisterDto.getEvent().getFirstOccurrenceDateTime());
        eventEntity.setId(formRegisterDto.getEvent().getId());

        return eventEntity;
    }

    @Override
    public EventEntity formDetailsDtoToEventEntity(FormDetailsDto formDetailsDto) {
        EventEntity eventEntity = new EventEntity();

        Set<StandDetailDto> standsDto;
        Set<StandEntity> standsEntity = new HashSet<>();
        standsDto = formDetailsDto.getStandsList();
        for (StandDetailDto standDto : standsDto){
            StandEntity standEntity = new StandEntity();
            standEntity.setIndex(standDto.getIndex());
            standEntity.setId(standDto.getId());
            standEntity.setEvent(eventEntity);
            standEntity.setDescription(standDto.getDescription());
            standEntity.setTotalAgents(standDto.getTotalAgents());
            Set<ProductEntity> productEntities = new HashSet<>();
            for (ProductDetailDto productDto : standDto.getProductsList()){
                ProductEntity productEntity = new ProductEntity();
                productEntity.setId(productDto.getId());
                productEntity.setDescription(productDto.getDescription());
                productEntity.setPrice(productDto.getPrice());
                productEntity.setStand(standEntity);
                productEntities.add(productEntity);
            }
            standEntity.setProductsList(productEntities);
            if (standDto.getAgentsList() != null){
                Set<StandAgentEntity> standAgentEntities = new HashSet<>();
                for (StandAgentDto standAgentDto : standDto.getAgentsList()){
                    StandAgentEntity standAgentEntity = new StandAgentEntity();
                    standAgentEntity.setId(standAgentDto.getId());
                    standAgentEntity.setName(standAgentDto.getAgentName());
                    standAgentEntity.setStand(standEntity);
                    standAgentEntities.add(standAgentEntity);
                }
                standEntity.setAgentsList(standAgentEntities);
            }
            standsEntity.add(standEntity);
        }
        eventEntity.setStandsList(standsEntity);
        eventEntity.setName(formDetailsDto.getEvent().getEventName());
        eventEntity.setPhoto(formDetailsDto.getEvent().getPhoto());
        eventEntity.setTotalAgents(formDetailsDto.getEvent().getTotalAgents());
        eventEntity.setAdminName(formDetailsDto.getAdmin().getName());
        eventEntity.setAvatar(formDetailsDto.getAdmin().getAvatar());
        eventEntity.setLogin(formDetailsDto.getAdmin().getLogin());
        eventEntity.setPassword(formDetailsDto.getAdmin().getPassword());
        eventEntity.setPhoto(formDetailsDto.getEvent().getPhoto());
        eventEntity.setDuration(formDetailsDto.getEvent().getDuration());
        eventEntity.setDescription(formDetailsDto.getEvent().getDescription());
        eventEntity.setFirstOccurrenceDateTime(formDetailsDto.getEvent().getFirstOccurrenceDateTime());
        eventEntity.setId(formDetailsDto.getEvent().getId());

        if (formDetailsDto.getAgentsList() != null) {
            Set<EventAgentEntity> cashierAgentEntities = new HashSet<>();
            for (EventAgentDto agentDto : formDetailsDto.getAgentsList()) {
                EventAgentEntity agentEntity = new EventAgentEntity(agentDto.getId(), agentDto.getAgentName(),
                        eventEntity);
                cashierAgentEntities.add(agentEntity);
            }
            eventEntity.setAgentsList(cashierAgentEntities);
        }

        return eventEntity;

    }

    @Override
    public ResponseEntity<FormDetailsDto> getEvent(UUID id) {

        EventEntity eventEntity = eventRepository.findById(id).get();
        FormDetailsDto formDetailsDto = eventEntityToFormDetailsDto(eventEntity);
        return ResponseEntity.status(HttpStatus.OK).body(formDetailsDto);
    }

    @Override
    public ResponseEntity<StandAgentDto> newStandAgent() {

        StandAgentEntity standAgentEntity = new StandAgentEntity();
        standAgentEntity = standAgentRepository.save(standAgentEntity);

        StandAgentDto standAgentDto = new StandAgentDto();
        BeanUtils.copyProperties(standAgentEntity, standAgentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(standAgentDto);
    }

    @Override
    public ResponseEntity<EventAgentDto> newEventAgent() {

        EventAgentEntity eventAgentEntity = new EventAgentEntity();
        eventAgentEntity = eventAgentRepository.save(eventAgentEntity);

        EventAgentDto eventAgentDto = new EventAgentDto();
        BeanUtils.copyProperties(eventAgentEntity, eventAgentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventAgentDto);
    }

}
