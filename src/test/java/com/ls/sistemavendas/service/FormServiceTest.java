package com.ls.sistemavendas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ls.sistemavendas.Entity.*;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.exceptions.EventAtSameTimeRuntimeException;
import com.ls.sistemavendas.exceptions.EventRepeatedRuntimeException;
import com.ls.sistemavendas.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class FormServiceTest {

    @MockBean
    private EventRepository eventRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void register() throws IOException {
        final var json = Paths.get("src", "test", "resources", "input.json");
        final var formRegisterDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json.toFile(), FormRegisterDto.class);

        EventEntity eventEntity = formRegisterDtoToEventEntity(formRegisterDto);
        eventEntity = eventRepository.save(eventEntity);

        if (eventRepository.existsByPeriod(formRegisterDto.getEvent().getFirstOccurrenceDateTime(),
                formRegisterDto.getEvent().getDuration())){
            throw new EventAtSameTimeRuntimeException("{\n  firstOccurrenceDateTime: Use outro período." +
                    "Porque já existe outro evento ocorrendo nesse período.\n}");
        }

        if (eventRepository.existsByName(formRegisterDto.getEvent().getEventName())) {

            throw new EventRepeatedRuntimeException("{\n  eventName: Escolha outro nome para o evento! " +
                    "Porque este nome já foi usado.\n}");
        }

        EventEntity eventEntity2 = formRegisterDtoToEventEntity(formRegisterDto);
        eventEntity2 = eventRepository.save(eventEntity2);
        FormDetailsDto formDetailsDto = eventEntityToFormDetailsDto(eventEntity2);



    }

    @Test
    void test() throws IOException {
        final var json = Paths.get("src", "test", "resources", "input.json");
        final var formDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json.toFile(), FormRegisterDto.class);

        Set<ConstraintViolation<FormRegisterDto>> violations = validator.validate(formDto);
        assertThat(violations.size()).isEqualTo(1);
    }

/*
    @Test
    public void save(@Valid FormRegisterDto formRegisterDto) {
        EventEntity eventEntity = formDtoToEntity(formRegisterDto);
        eventRepository.save(eventEntity);

        assertEquals(1.0, eventRepository.count());

    }*/

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
        eventEntity.setPassword(formRegisterDto.getAdmin().getPassword());
        eventEntity.setPhoto(formRegisterDto.getEvent().getPhoto());
        eventEntity.setDuration(formRegisterDto.getEvent().getDuration());
        eventEntity.setDescription(formRegisterDto.getEvent().getDescription());
        eventEntity.setFirstOccurrenceDateTime(formRegisterDto.getEvent().getFirstOccurrenceDateTime());
        eventEntity.setId(formRegisterDto.getEvent().getId());

        return eventEntity;
    }

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

}
