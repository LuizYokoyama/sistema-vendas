package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.dto.StandDetailDto;
import com.ls.sistemavendas.dto.TransactionDto;
import com.ls.sistemavendas.service.IStandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@Api(value="Stands API REST")
@CrossOrigin(origins = "*")
public class StandController {

    @Autowired
    private IStandService standService;

    @GetMapping("/stand/{id}")
    @ApiOperation(value = "Get details of the stand")
    public ResponseEntity<StandDetailDto> getStand(@PathVariable(value = "id") UUID id){

        if (!standService.findById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return standService.getStandDetails(id);
    }

    @PostMapping("/sell")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add new transactions items to the participant.")
    public ResponseEntity<TransactionDto> sell(@RequestBody TransactionDto transactionDto){
        return standService.newTransaction(transactionDto);
    }
}
