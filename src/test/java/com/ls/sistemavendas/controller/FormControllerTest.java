package com.ls.sistemavendas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.sistemavendas.dto.*;
import com.ls.sistemavendas.service.FormService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class FormControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FormService formService;

    @Test
    public void add() throws Exception{
        String str = "2022-09-02T19:09:27.096Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        AdminDto adminDto = new AdminDto("string", "string", "string", "string");
        EventDto eventDto = new EventDto(0L, "string", "string", "string", 0, dateTime, 0);
        ProductDto productDto = new ProductDto(0L, "string", 0);
        Set<ProductDto> productDtoSet = new HashSet<>();
        productDtoSet.add(productDto);
        StandDto standDto = new StandDto(0l, 0, "string", 0, productDtoSet );
        Set<StandDto> standDtoSet = new HashSet<>();
        standDtoSet.add(standDto);
        FormDto formDto = new FormDto(eventDto, adminDto, standDtoSet);
        when(formService.save(any(FormDto.class))).thenReturn(formDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String formDtoJSON = objectMapper.writeValueAsString(formDto);

        ResultActions result = mockMvc.perform(post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(formDtoJSON)
        );
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.admin").value("{\n" +
                        "    \"avatar\": \"string\",\n" +
                        "    \"login\": \"string\",\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"password\": \"string\"\n" +
                        "  }"))
                .andExpect(jsonPath("$.event").value("{\n" +
                        "    \"description\": \"string\",\n" +
                        "    \"duration\": 0,\n" +
                        "    \"eventName\": \"string\",\n" +
                        "    \"firstOccurrenceDateTime\": \"2022-09-02T19:09:27.096Z\",\n" +
                        "    \"id\": 0,\n" +
                        "    \"photo\": \"string\",\n" +
                        "    \"totalAgents\": 0\n" +
                        "  }"))
                .andExpect(jsonPath("$.standsList").value("[\n" +
                        "    {\n" +
                        "      \"description\": \"string\",\n" +
                        "      \"id\": 0,\n" +
                        "      \"index\": 0,\n" +
                        "      \"productsList\": [\n" +
                        "        {\n" +
                        "          \"description\": \"string\",\n" +
                        "          \"id\": 0,\n" +
                        "          \"price\": 0\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"totalAgents\": 0\n" +
                        "    }\n" +
                        "  ]"));
    }

}
