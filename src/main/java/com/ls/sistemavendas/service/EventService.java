package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.*;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.exceptions.EventAtSameTimeRuntimeException;
import com.ls.sistemavendas.exceptions.EventRepeatedRuntimeException;
import com.ls.sistemavendas.exceptions.UserNameAlreadyExistsRuntimeException;
import com.ls.sistemavendas.repository.EventAgentRepository;
import com.ls.sistemavendas.repository.EventRepository;
import com.ls.sistemavendas.repository.StandAgentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;


@Service
@Validated
public class EventService implements UserDetailsService, IEventService {

    final int SHORT_ID_LENGTH = 8;

    @Autowired
    private EventAgentRepository eventAgentRepository;

    @Autowired
    private StandAgentRepository standAgentRepository;

    private EventRepository eventRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KeyCloakService keyCloakService;

    @Autowired
    @Override
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<FormDetailsDto> register(@Valid FormRegisterDto formRegisterDto) {

        if (eventRepository.existsByPeriod(formRegisterDto.getEvent().getFirstOccurrenceDateTime(),
                formRegisterDto.getEvent().getDuration())){
            throw new EventAtSameTimeRuntimeException("{\n  firstOccurrenceDateTime: Use outro período." +
                    "Porque já existe outro evento ocorrendo nesse período.\n}");
        }

        if (eventRepository.existsByName(formRegisterDto.getEvent().getEventName())){

            throw new EventRepeatedRuntimeException("{\n  eventName: Escolha outro nome para o evento! " +
                    "Porque este nome já foi usado.\n}");
        }

        ResponseEntity<String> keycloak =  keyCloakService.addUserAdmin(formRegisterDto.getAdmin());
        if (keycloak.getStatusCode() != HttpStatus.CREATED){
            if (keycloak.getStatusCode() == HttpStatus.CONFLICT){
                throw new UserNameAlreadyExistsRuntimeException("Use outro login! Porque este já foi usado!");
            }
            throw new RuntimeException(keycloak.toString());

        }

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
            standDto.setStandTotalAgents(standEntity.getTotalAgents());
            standDtos.add(standDto);
        }
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
            standDto.setTotalTransactions(standEntity.getTotalTransactions());
            standDto.setStandTotalAgents(standEntity.getTotalAgents());
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
            standEntity.setTotalAgents(standDto.getStandTotalAgents());
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
        eventEntity.setPassword(passwordEncoder.encode(formRegisterDto.getAdmin().getPassword()));
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
            standEntity.setTotalTransactions(standDto.getTotalTransactions());
            standEntity.setDescription(standDto.getDescription());
            standEntity.setTotalAgents(standDto.getStandTotalAgents());
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
        eventEntity.setPassword(passwordEncoder.encode(formDetailsDto.getAdmin().getPassword()));
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
    public ResponseEntity<String> newStandAgent() {

        StandAgentEntity standAgentEntity = new StandAgentEntity();
        standAgentEntity.setId(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
        standAgentEntity = standAgentRepository.save(standAgentEntity);

        ResponseEntity<String> userKC = keyCloakService.addUserStandAgent(standAgentEntity.getId());

        if (userKC.getStatusCode() != HttpStatus.CREATED ){
            throw new RuntimeException(userKC.toString());
        }

        StandAgentDto standAgentDto = new StandAgentDto();
        BeanUtils.copyProperties(standAgentEntity, standAgentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(standAgentDto.getId());
    }

    @Override
    public ResponseEntity<String> newEventAgent() {

        EventAgentEntity eventAgentEntity = new EventAgentEntity();
        eventAgentEntity.setId(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
        eventAgentEntity = eventAgentRepository.save(eventAgentEntity);

        ResponseEntity<String> userKC = keyCloakService.addUserStandAgent(eventAgentEntity.getId());

        if (userKC.getStatusCode() != HttpStatus.CREATED ){
            throw new RuntimeException(userKC.toString());
        }

        EventAgentDto eventAgentDto = new EventAgentDto();
        BeanUtils.copyProperties(eventAgentEntity, eventAgentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventAgentDto.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EventEntity eventEntity = eventRepository.findByLogin(username)
                .orElseThrow( () -> new UsernameNotFoundException("Verifique o login, porque "
                        + username + " não foi encontrado!"));
        return eventEntity;
    }
}
