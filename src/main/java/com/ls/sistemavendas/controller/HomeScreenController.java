package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.HomeScreenEventDto;
import com.ls.sistemavendas.service.IHomeScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value="Home Events API REST")
@CrossOrigin(origins = "*")
public class HomeScreenController {

    @Autowired
    IHomeScreenService homeScreenService;

    @GetMapping("/start-events")
    @ApiOperation(value = "Simplified list of all events for home screen")
    public List<HomeScreenEventDto> startList(){
        return homeScreenService.findAllEventsStart();
    }
}
