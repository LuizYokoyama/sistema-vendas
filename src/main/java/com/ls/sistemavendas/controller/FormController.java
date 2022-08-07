package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.Entity.EventEntity;
import com.ls.sistemavendas.dto.FormDto;
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
    public List<FormDto> listFull(){
        return formService.findAllFull();
    }

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new event")
    public ResponseEntity<FormDto> add(@RequestBody FormDto formDto){
        return formService.save(formDto);
    }

    @PutMapping("/event/{id}")
    @ApiOperation(value = "Update details of the event form")
    public ResponseEntity<FormDto> updateEvent(@PathVariable(value = "id") UUID id,
                                              @RequestBody FormDto formDto){

        Optional<EventEntity> eventEntityOptional = formService.findById(id);
        if (!eventEntityOptional.isPresent()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado!");
        }

        formDto.getEvent().setId(id);

        return ResponseEntity.status(HttpStatus.OK).body(formService.save(formDto).getBody());
    }
}
