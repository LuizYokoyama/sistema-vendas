package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.FormDetailsDto;
import com.ls.sistemavendas.dto.FormRegisterDto;
import com.ls.sistemavendas.service.IFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Api(value="Events Form API REST")
@CrossOrigin(origins = "*")
public class FormController {

    @Autowired
    IFormService formService;

    @GetMapping("/events-full")
    @ApiOperation(value = "List of all events")
    public List<FormRegisterDto> listFull(){
        return formService.findAllFull();
    }

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new event")
    public ResponseEntity<FormDetailsDto> addEvent(@RequestBody FormRegisterDto formRegisterDto){
        return formService.register(formRegisterDto);
    }

    @PutMapping("/event/{id}")
    @ApiOperation(value = "Update details of the event form")
    public ResponseEntity<FormDetailsDto> updateEvent(@PathVariable(value = "id") UUID id, @RequestBody FormDetailsDto formDetailsDto){

        Optional<EventEntity> eventEntityOptional = formService.findById(id);
        if (!eventEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        formDetailsDto.getEvent().setId(id);
        return formService.update(formDetailsDto);
    }

    @GetMapping("/event/{id}")
    @ApiOperation(value = "Get details of the event form")
    public ResponseEntity<FormDetailsDto> getEvent(@PathVariable(value = "id") UUID id){

        if (!formService.findById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return formService.getEvent(id);
    }

    @PostMapping("/agent/new-stand-agent")
    @ApiOperation(value = "Get new stand agent hashcode id")
    public ResponseEntity<String> getNewStandAgent(){
        return formService.newStandAgent();
    }

    @PostMapping("/agent/new-event-agent")
    @ApiOperation(value = "Get new event agent hashcode id")
    public  ResponseEntity<String> getNewEventAgent(){
        return formService.newEventAgent();
    }
}
