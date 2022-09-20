package com.ls.sistemavendas.service;

import com.ls.sistemavendas.dto.HomeScreenEventDto;
import com.ls.sistemavendas.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class HomeScreenServiceTest {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    @InjectMocks
    private HomeScreenService homeScreenService;

    private List<HomeScreenEventDto> homeScreenEventDtoList;

    @BeforeEach
    public void setUp(){

        homeScreenEventDtoList = new ArrayList<>();

    }

    @AfterEach
    public void tearDown(){

        homeScreenEventDtoList = null;

    }

    @Test
    void returnAllEventsList(){

        when(eventRepository.getAllEventsShortList()).thenReturn(homeScreenEventDtoList);

        var responsehomeScreenEventDtoList = homeScreenService.findAllEventsStart();

        verify(eventRepository, times(1)).getAllEventsShortList();
        assertEquals(responsehomeScreenEventDtoList, homeScreenEventDtoList);
    }
}
