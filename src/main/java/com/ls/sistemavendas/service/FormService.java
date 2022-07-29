package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.FormEntity;
import com.ls.sistemavendas.dto.FormDto;
import com.ls.sistemavendas.repository.FormRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormService implements IFormService {

    @Autowired
    private FormRepository formRepository;

    @Override
    public FormDto save(FormDto formDto) {
        FormEntity formEntity = new FormEntity();
        BeanUtils.copyProperties(formDto, formEntity);
        formEntity = formRepository.save(formEntity);
        BeanUtils.copyProperties(formEntity, formDto);
        return formDto;
    }

    @Override
    public List findAll() {

        return formRepository.findAll();
    }
}