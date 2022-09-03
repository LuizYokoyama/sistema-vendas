package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.service.IParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(value="Participants API REST")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyAuthority('ROLE_EVENT_AGENT')")
public class ParticipantController {

    @Autowired
    IParticipantService participantService;

    @PostMapping("/participant")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new participant.")
    public ResponseEntity<ParticipantDetailDto> create(@RequestBody ParticipantDto participantDto){
        return participantService.newParticipant(participantDto);
    }

    @PostMapping("/payment")
    @ApiOperation(value = "Add a participant's payment to the payments list.")
    public ResponseEntity<PaymentDetailDto> addPayment(@RequestBody PaymentDto paymentDto){
        return participantService.newPayment(paymentDto);
    }

    @GetMapping("participant-release/{code}")
    @ApiOperation(value = "Get the participant at the payments list.")
    public ResponseEntity<ParticipantReleasedDto> release(@PathVariable(value = "code") String code ){
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
