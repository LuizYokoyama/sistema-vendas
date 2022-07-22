package com.ls.sistemavendas.controller;

import com.ls.sistemavendas.model.Event;
import com.ls.sistemavendas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> listar(){
        return eventRepository.findAll();
    }

    @GetMapping("/error1")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String showError1(){
        return "Teste ERRO 1 Teste ERRO 1 Teste ERRO 1";
    }

    @GetMapping("/error2")
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String showError2(){
        return "Teste ERRO 2 Teste ERRO 2 Teste ERRO 2";
    }

    @GetMapping("/error3")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String showError3(){
        return "Teste ERRO 3 Teste ERRO 3 Teste ERRO 3";
    }

    @GetMapping("/error4")
    @ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public String showError4(){
        return "Teste ERRO 4 Teste ERRO 4 Teste ERRO 4";
    }

    @GetMapping("/error5")
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public String showError5(){
        return "Teste ERRO 5 Teste ERRO 5 Teste ERRO 5";
    }

    @GetMapping("/error6")
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public String showError6(){
        return "Teste ERRO 6 Teste ERRO 6 Teste ERRO 6";
    }

    @GetMapping("/error7")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String showError7(){
        return "Teste ERRO 7 Teste ERRO 7 Teste ERRO 7";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event add(@RequestBody Event event){
        return eventRepository.save(event);
    }

    @GetMapping("/metrics")
    public String getMetrics(){
        return "# TYPE requests counter\nresquests 1" + "\n# EOF";
    }
}
