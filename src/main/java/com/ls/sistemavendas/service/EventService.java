package com.ls.sistemavendas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.sistemavendas.Entity.*;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.exceptions.*;
import com.ls.sistemavendas.repository.EventAgentRepository;
import com.ls.sistemavendas.repository.EventRepository;
import com.ls.sistemavendas.repository.StandAgentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;


@Service
@Validated
public class EventService implements IEventService {

    final int SHORT_ID_LENGTH = 8;

    final static String GRANT_TYPE = "password";
    final static String CLIENT_ID = "quermese_admin";
    final static String CLIENT_SECRET = "zbji9pCixGxl1NByrdJJG3zYqJPL4mmN";

    @Autowired
    private EventAgentRepository eventAgentRepository;

    @Autowired
    private StandAgentRepository standAgentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KeyCloakService keyCloakService;


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
        formRegisterDto.getAdmin().setAdminId(
                keyCloakService.getUser(formRegisterDto.getAdmin().getLogin()).get(0).getId()
        );

        EventEntity eventEntity = formRegisterDtoToEventEntity(formRegisterDto);
        eventEntity = eventRepository.save(eventEntity);
        FormDetailsDto formDetailsDto = eventEntityToFormDetailsDto(eventEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(formDetailsDto);
    }

    @Override
    @Transactional
    public ResponseEntity<FormDetailsDto> update(FormDetailsDto formDetailsDto) {

        Optional<EventEntity> eventEntityOptional = eventRepository.findById(formDetailsDto.getEvent().getId());

        if (eventEntityOptional.isEmpty()){
            throw new EventNotFoundRuntimeException("Verifique o id do evento! Porque este não foi encontrado.");
        }

        EventEntity eventEntity = eventEntityOptional.get();
        formDetailsDto.getAdmin().setAdminId(eventEntity.getAdminId());

        keyCloakService.updateUserAdmin(formDetailsDto.getAdmin());

        eventEntity = formDetailsDtoToEventEntity(formDetailsDto);
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
        formRegisterDto.setAdmin(new AdminDto(eventEntity.getAdminName(), eventEntity.getAdminId(),
                eventEntity.getLogin(), "", eventEntity.getAvatar()));

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
                    StandAgentDto standAgentDto = new StandAgentDto(standAgentEntity.getId(), standAgentEntity.getName(),
                            standAgentEntity.getKeycloakId());
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
        formDetailsDto.setAdmin(new AdminDto(eventEntity.getAdminName(), eventEntity.getAdminId(), eventEntity.getLogin(),
                "", eventEntity.getAvatar()));
        Set<EventAgentDto> agentDtos = new HashSet<>();
        if (eventEntity.getAgentsList() != null){
            for (EventAgentEntity agentEntity : eventEntity.getAgentsList()) {
                EventAgentDto agentDto = new EventAgentDto(agentEntity.getId(), agentEntity.getName(),
                        agentEntity.getKeycloakId());
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
        eventEntity.setAdminId(formRegisterDto.getAdmin().getAdminId());
        eventEntity.setAvatar(formRegisterDto.getAdmin().getAvatar());
        eventEntity.setLogin(formRegisterDto.getAdmin().getLogin());
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
                    standAgentEntity.setKeycloakId(standAgentDto.getKeycloakId());
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
        eventEntity.setAdminId(formDetailsDto.getAdmin().getAdminId());
        eventEntity.setAvatar(formDetailsDto.getAdmin().getAvatar());
        eventEntity.setLogin(formDetailsDto.getAdmin().getLogin());
        eventEntity.setPhoto(formDetailsDto.getEvent().getPhoto());
        eventEntity.setDuration(formDetailsDto.getEvent().getDuration());
        eventEntity.setDescription(formDetailsDto.getEvent().getDescription());
        eventEntity.setFirstOccurrenceDateTime(formDetailsDto.getEvent().getFirstOccurrenceDateTime());
        eventEntity.setId(formDetailsDto.getEvent().getId());

        if (formDetailsDto.getAgentsList() != null) {
            Set<EventAgentEntity> cashierAgentEntities = new HashSet<>();
            for (EventAgentDto agentDto : formDetailsDto.getAgentsList()) {
                EventAgentEntity agentEntity = new EventAgentEntity(agentDto.getId(), agentDto.getAgentName(),
                        eventEntity, agentDto.getKeycloakId());
                cashierAgentEntities.add(agentEntity);
            }
            eventEntity.setAgentsList(cashierAgentEntities);
        }

        return eventEntity;

    }

    @Override
    public ResponseEntity<FormDetailsDto> getEvent(UUID id) {

        Optional<EventEntity> eventEntityOptional = eventRepository.findById(id);
        if (eventEntityOptional.isEmpty()){
            throw new EventNotFoundRuntimeException("Verifique o id do evento! Porque este não foi encontrado!");
        }
        EventEntity eventEntity = eventEntityOptional.get();
        FormDetailsDto formDetailsDto = eventEntityToFormDetailsDto(eventEntity);
        return ResponseEntity.status(HttpStatus.OK).body(formDetailsDto);
    }

    @Override
    public ResponseEntity<StandAgentDto> newStandAgent() {

        StandAgentEntity standAgentEntity = new StandAgentEntity();
        standAgentEntity.setId(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));


        ResponseEntity<String> userKC = keyCloakService.addUserStandAgent(standAgentEntity.getId());

        if (userKC.getStatusCode() != HttpStatus.CREATED){
            if (userKC.getStatusCode() == HttpStatus.CONFLICT){
                standAgentEntity.setId(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
                userKC = keyCloakService.addUserStandAgent(standAgentEntity.getId());
            }
            throw new RuntimeException(userKC.toString());
        }

        standAgentEntity.setKeycloakId(
                keyCloakService.getUser(standAgentEntity.getId()).get(0).getId()
        );

        standAgentEntity = standAgentRepository.save(standAgentEntity);

        StandAgentDto standAgentDto = new StandAgentDto();
        standAgentDto.setKeycloakId(standAgentEntity.getKeycloakId());
        standAgentDto.setId(standAgentEntity.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(standAgentDto);
    }

    @Override
    public ResponseEntity<EventAgentDto> newEventAgent() {

        EventAgentEntity eventAgentEntity = new EventAgentEntity();
        eventAgentEntity.setId(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));


        ResponseEntity<String> userKC = keyCloakService.addUserEventAgent(eventAgentEntity.getId());

        if (userKC.getStatusCode() != HttpStatus.CREATED){
            if (userKC.getStatusCode() == HttpStatus.CONFLICT){  //case id already exists, try a new one
                eventAgentEntity.setId(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
                userKC = keyCloakService.addUserEventAgent(eventAgentEntity.getId());
            }
            throw new RuntimeException(userKC.toString());
        }

        eventAgentEntity.setKeycloakId(
                keyCloakService.getUser(eventAgentEntity.getId()).get(0).getId()
        );

        eventAgentEntity = eventAgentRepository.save(eventAgentEntity);

        EventAgentDto eventAgentDto = new EventAgentDto();
        eventAgentDto.setKeycloakId(eventAgentEntity.getKeycloakId());
        eventAgentDto.setId(eventAgentEntity.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventAgentDto);
    }

    @Override
    public ResponseEntity<agentKeycloakResponseDto> agentLogin(String username) {

        String url ="http://localhost:8180/auth/realms/quermesse/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("username", username);
        map.add("password", username);
        map.add("grant_type", GRANT_TYPE);
        map.add("client_secret", CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class );
        agentKeycloakResponseDto agentKeycloakResponseDto;
        try {
            if (response.getBody() == null){
                throw new BadCredentialsRuntimeException("Verifique o código do agente.");
            }
            agentKeycloakResponseDto = new ObjectMapper().readValue(
                    response.getBody().replace("not-before-policy", "not_before_policy"),
                    agentKeycloakResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        agentKeycloakResponseDto.setNameNeed(false);
        if (agentKeycloakResponseDto.getScope().equals("AGENT_STAND")){
            var standAgentEntityOptional = standAgentRepository.findById(username);
            if (standAgentEntityOptional.isPresent()){
                if (standAgentEntityOptional.get().getStand() != null){
                    agentKeycloakResponseDto.setStandId(standAgentEntityOptional.get().getStand().getId());
                }
                if (standAgentEntityOptional.get().getName() == null){
                    agentKeycloakResponseDto.setNameNeed(true);
                }
            }
        } else if (agentKeycloakResponseDto.getScope().equals("AGENT_EVENT")){
            var eventAgentEntityOptional = eventAgentRepository.findById(username);
            if (eventAgentEntityOptional.isPresent()){
                if (eventAgentEntityOptional.get().getEvent() != null){
                    agentKeycloakResponseDto.setStandId(eventAgentEntityOptional.get().getEvent().getId());
                }
                if (eventAgentEntityOptional.get().getName() == null){
                    agentKeycloakResponseDto.setNameNeed(true);
                }
            }
        }

        return ResponseEntity.ok().body(agentKeycloakResponseDto);
    }

    public ResponseEntity<EventAgentDto> setEventAgentName(String code, String name) {

        Optional<EventAgentEntity> eventAgentEntityOptional = eventAgentRepository.findById(code);
        if (eventAgentEntityOptional.isEmpty()){
            throw new AgentCodeNotFoundRuntimeException("Verifique o código do agente do evento! Porque "
                    +code+ " não foi encontrado.");
        }
        EventAgentEntity eventAgentEntity = eventAgentEntityOptional.get();
        eventAgentEntity.setName(name);
        eventAgentEntity = eventAgentRepository.save(eventAgentEntity);
        EventAgentDto eventAgentDto = new EventAgentDto();
        BeanUtils.copyProperties(eventAgentEntity, eventAgentDto);
        return ResponseEntity.status(HttpStatus.OK).body(eventAgentDto);
    }

}
