package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.EventAgentDto;
import com.ls.sistemavendas.dto.FormDetailsDto;
import com.ls.sistemavendas.dto.FormRegisterDto;
import com.ls.sistemavendas.dto.StandAgentDto;
import com.ls.sistemavendas.service.IEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Api(value="Events Form API REST")
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class EventController {


    @Autowired
    IEventService eventService;

    @GetMapping("/events-full")
    @ApiOperation(value = "List of all events")
    public List<FormRegisterDto> listFull(){
        return eventService.findAllFull();
    }


    @PutMapping("/event/{id}")
    @ApiOperation(value = "Update details of the event form")
    public ResponseEntity<FormDetailsDto> updateEvent(@PathVariable(value = "id") UUID id, @RequestBody FormDetailsDto formDetailsDto){

        formDetailsDto.getEvent().setId(id);
        return eventService.update(formDetailsDto);
    }

    @GetMapping("/event/{id}")
    @ApiOperation(value = "Get details of the event form")
    public ResponseEntity<FormDetailsDto> getEvent(@PathVariable(value = "id") UUID id){

        return eventService.getEvent(id);
    }



    @PostMapping("/agent/new-stand-agent")
    @ApiOperation(value = "Get new stand agent hashcode id")
    public ResponseEntity<StandAgentDto> getNewStandAgent(){
        return eventService.newStandAgent();
    }

    @PostMapping("/agent/new-event-agent")
    @ApiOperation(value = "Get new event agent hashcode id")
    public  ResponseEntity<EventAgentDto> getNewEventAgent(){
        return eventService.newEventAgent();
    }
}
