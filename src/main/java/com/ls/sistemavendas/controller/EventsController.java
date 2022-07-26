package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.model.Event;
import com.ls.sistemavendas.repository.EventRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value="Events API REST")
@CrossOrigin(origins = "*")
public class EventsController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/events")
    @ApiOperation(value = "List of all events")
    public List<Event> list(){
        return eventRepository.findAll();
    }


    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new event")
    public Event add(@RequestBody Event event){
        return eventRepository.save(event);
    }

}
