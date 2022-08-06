package com.ls.sistemavendas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.Entity.ProductEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.dto.FormDto;
import com.ls.sistemavendas.dto.ProductDto;
import com.ls.sistemavendas.dto.StandDto;
import com.ls.sistemavendas.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class FormServiceTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void teste() throws IOException {
        final var json = Paths.get("src", "test", "resources", "input.json");
        final var formDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json.toFile(), FormDto.class);
        save(formDto);
    }

    @ParameterizedTest
    public void save(FormDto formDto) {
        EventEntity eventEntity = new EventEntity();
        Set<StandDto> standsDto;
        Set<StandEntity> standsEntity = new HashSet<>();
        standsDto = formDto.getStandsList();
        for (StandDto standDto : standsDto){
            StandEntity standEntity = new StandEntity();
            standEntity.setIndex(standDto.getIndex());
            standEntity.setDescription(standDto.getDescription());
            standEntity.setTotalAgents(standDto.getTotalAgents());
            Set<ProductEntity> productEntities = new HashSet<>();
            for (ProductDto productDto : standDto.getProductsList()){
                ProductEntity productEntity = new ProductEntity();
                productEntity.setDescription(productDto.getDescription());
                productEntity.setPrice(productDto.getPrice());
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

        BeanUtils.copyProperties(eventRepository.save(eventEntity), formDto);

        assertEquals(1.0, eventRepository.count());


    }
}
