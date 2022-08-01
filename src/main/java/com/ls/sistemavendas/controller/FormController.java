package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.FormDto;
import com.ls.sistemavendas.service.IFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public FormDto add(@RequestBody FormDto formDto){
        return formService.save(formDto);
    }

}
