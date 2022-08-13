package com.ls.sistemavendas.service;

import com.ls.sistemavendas.dto.StandDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IStandService {

    ResponseEntity<StandDto> getStandDetails(UUID id);

    
}
