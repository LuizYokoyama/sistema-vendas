package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.CashierDto;
import com.ls.sistemavendas.dto.ParticipantDetailDto;
import com.ls.sistemavendas.dto.ParticipantDto;
import com.ls.sistemavendas.dto.ParticipantSummaryDto;
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

    @PostMapping("/participant")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new participant.")
    public ResponseEntity<ParticipantDetailDto> sell(@RequestBody ParticipantDto participantDto){
        return participantService.newParticipant(participantDto);
    }

    @GetMapping("participant-release/{code}")
    @ApiOperation(value = "Get the participant at released list.")
    public ResponseEntity<ParticipantSummaryDto> release(@PathVariable(value = "code") String code ){
        if (!participantService.findByCode(code).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return participantService.getParticipantReleased(code);
    }

    @GetMapping("participant-cashier/{code}")
    @ApiOperation(value = "Get the participant's purchased products list for the cashier.")
    public ResponseEntity<CashierDto> cashier(@PathVariable(value = "code") String code ) {
        if (!participantService.findByCode(code).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return participantService.getParticipantCashier(code);
    }

}
