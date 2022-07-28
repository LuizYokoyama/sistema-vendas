package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.EventDto;
import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService implements IEventService{

    @Autowired
    private EventRepository eventRepository;

    @Override
    public EventDto save(EventDto eventDto) {
        EventEntity eventEntity = new EventEntity();
        //BeanUtils.copyProperties(eventDto, eventEntity);
        eventEntity = eventRepository.save(eventDto.getEventEntity());
        BeanUtils.copyProperties(eventEntity, eventDto);
        return eventDto;
    }

    @Override
    public List findAll() {

        List<EventDto> eventDtoList = new ArrayList<>();
        List<EventEntity> eventEntityList = eventRepository.findAll();
        EventEntity eventEntity = eventEntityList.get(0);
        EventDto eventDto = new EventDto();
        BeanUtils.copyProperties(eventEntity, eventDto);
        eventDtoList.add(eventDto);
        return eventDtoList;
    }
}
