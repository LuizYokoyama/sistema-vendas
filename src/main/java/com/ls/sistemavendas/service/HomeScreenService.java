package com.ls.sistemavendas.service;

import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeScreenService implements IHomeScreenService {

    @Autowired
    EventRepository eventRepository;
    @Override
    public List findAllEventsStart() {
        return eventRepository.getAllEventsShortList();
    }


}
