package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.Entity.ProductEntity;
import com.ls.sistemavendas.Entity.StandEntity;
import com.ls.sistemavendas.dto.FormDto;
import com.ls.sistemavendas.dto.ProductDto;
import com.ls.sistemavendas.dto.StandDto;
import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class FormService implements IFormService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public FormDto save(FormDto formDto) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<FormDto>> violations = validator.validate(formDto);

        if (!violations.isEmpty()){

            return null;
        }

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

        return formDto;
    }

    @Override
    public List findAllFull() {
        return eventRepository.findAll();
    }

}
