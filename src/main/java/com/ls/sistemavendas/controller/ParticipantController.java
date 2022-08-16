package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.TransactionDto;
import com.ls.sistemavendas.service.IParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(value="Participants API REST")
@CrossOrigin(origins = "*")
public class ParticipantController {

    @Autowired
    IParticipantService participantService;

    @PostMapping("/sell")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add new transactions items to the participant.")
    public ResponseEntity<TransactionDto> sell(@RequestBody TransactionDto transactionDto){
        return participantService.newTransaction(transactionDto);
    }

    @PostMapping("/participant")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new participant.")
    public ResponseEntity<ParticipantDto> sell(@RequestBody ParticipantDto participantDto){
        return participantService.newParticipant(participantDto);
    }
}
