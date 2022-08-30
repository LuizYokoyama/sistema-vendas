package com.ls.sistemavendas.service;

import com.ls.sistemavendas.Entity.EventAgentEntity;
import com.ls.sistemavendas.repository.EventAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EventAgentService implements UserDetailsService {

    @Autowired
    private EventAgentRepository eventAgentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EventAgentEntity eventAgentEntity = eventAgentRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Verifique o código do agente, porque "
                        + username + " não foi encontrado!"));

        return eventAgentEntity;
    }
}
