package com.ls.sistemavendas.service;

import com.ls.sistemavendas.dto.FormDto;

import java.util.List;

public interface IFormService {

    FormDto save(FormDto formDto);
    List findAllFull();



}
