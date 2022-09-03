package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.FormDetailsDto;
import com.ls.sistemavendas.dto.FormRegisterDto;
import com.ls.sistemavendas.dto.HomeScreenEventDto;
import com.ls.sistemavendas.service.IFormService;
import com.ls.sistemavendas.service.IHomeScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value="Home Events API REST")
@CrossOrigin(origins = "*")
public class HomeScreenController {

    @Autowired
    IHomeScreenService homeScreenService;

    @Autowired
    IFormService formService;


    @GetMapping("/start-events")
    @ApiOperation(value = "Simplified list of all events for home screen")
    public List<HomeScreenEventDto> startList(){
        return homeScreenService.findAllEventsStart();
    }

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new event")
    public ResponseEntity<FormDetailsDto> addEvent(@RequestBody FormRegisterDto formRegisterDto){
        return formService.register(formRegisterDto);
    }

}
