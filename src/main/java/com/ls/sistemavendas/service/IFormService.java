package com.ls.sistemavendas.service;

import com.ls.sistemavendas.dto.FormDto;

import javax.validation.Valid;
import java.util.List;

public interface IFormService {

    FormDto save(@Valid FormDto formDto);
    List findAllFull();



}
