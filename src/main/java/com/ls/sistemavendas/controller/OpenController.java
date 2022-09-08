package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.AdminDto;
import com.ls.sistemavendas.dto.FormDetailsDto;
import com.ls.sistemavendas.dto.FormRegisterDto;
import com.ls.sistemavendas.dto.HomeScreenEventDto;
import com.ls.sistemavendas.service.IFormService;
import com.ls.sistemavendas.service.IHomeScreenService;
import com.ls.sistemavendas.service.KeyCloakService;
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
public class OpenController {

    @Autowired
    IHomeScreenService homeScreenService;

    @Autowired
    IFormService formService;

    @Autowired
    KeyCloakService keyCloakService;


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

    @PostMapping("/user")
    @ApiOperation(value = "Post new user (test)")
    public ResponseEntity<String> addUser(@RequestBody AdminDto adminDto){

        return keyCloakService.addUser(adminDto);

    }

    @GetMapping("/user/{login}")
    @ApiOperation(value = "Get user (test)")
    public String getUser(@PathVariable(value = "login") String login){

        System.out.println("getAttributes: "+keyCloakService.getUser(login).get(0).getAttributes());
        System.out.println("getId: "+keyCloakService.getUser(login).get(0).getId());
        System.out.println("getAccess: "+keyCloakService.getUser(login).get(0).getAccess());
        System.out.println("getClientConsents: "+keyCloakService.getUser(login).get(0).getClientConsents());
        System.out.println("getClientRoles: "+keyCloakService.getUser(login).get(0).getClientRoles());
        System.out.println("getCredentials: "+keyCloakService.getUser(login).get(0).getCredentials());
        System.out.println("getUsername: "+keyCloakService.getUser(login).get(0).getUsername());
        System.out.println("getFirstName: "+keyCloakService.getUser(login).get(0).getFirstName());
        System.out.println("getLastName: "+keyCloakService.getUser(login).get(0).getLastName());
        System.out.println("getGroups: "+keyCloakService.getUser(login).get(0).getGroups());









        return "Done";

    }
}
