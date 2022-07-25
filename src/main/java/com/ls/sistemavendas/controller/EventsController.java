package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.model.Event;
import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> list(){
        return eventRepository.findAll();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event add(@RequestBody Event event){
        return eventRepository.save(event);
    }

}
