package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.EventDto;
import com.ls.sistemavendas.service.IEventService;
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
    IEventService eventService;

    @GetMapping("/events")
    @ApiOperation(value = "List of all events")
    public List<EventDto> list(){
        return eventService.findAll();
    }


    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new event")
    public EventDto add(@RequestBody EventDto eventDto){
        return eventService.save(eventDto);
    }

}
