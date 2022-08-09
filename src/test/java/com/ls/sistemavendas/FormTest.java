package com.ls.sistemavendas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ls.sistemavendas.dto.FormRegisterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class FormTest {
    @Test
    void teste() throws IOException {
        final var json = Paths.get("src", "test", "resources", "input.json");
        final var formDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json.toFile(), FormRegisterDto.class);
        Assertions.assertNotNull(formDto);


    }
}
