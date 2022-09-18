package com.ls.sistemavendas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.sistemavendas.config.Credentials;
import com.ls.sistemavendas.config.KeycloakConfig;
import com.ls.sistemavendas.dto.AdminDto;
import com.ls.sistemavendas.dto.AdminKeycloakResponseDto;
import com.ls.sistemavendas.dto.AgentKeycloakResponseDto;
import com.ls.sistemavendas.exceptions.BadCredentialsRuntimeException;
import com.ls.sistemavendas.repository.EventAgentRepository;
import com.ls.sistemavendas.repository.EventRepository;
import com.ls.sistemavendas.repository.StandAgentRepository;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class KeycloakService {

    final static String URL ="http://localhost:8180/auth/realms/quermesse/protocol/openid-connect/token";
    final static String GRANT_TYPE = "password";
    final static String CLIENT_ID = "quermese_admin";
    final static String CLIENT_SECRET = "zbji9pCixGxl1NByrdJJG3zYqJPL4mmN";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StandAgentRepository standAgentRepository;

    @Autowired
    private EventAgentRepository eventAgentRepository;

    public ResponseEntity<String> addUserAdmin(AdminDto adminDto){

        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(adminDto.getPassword());

        List<String> groupsList = new ArrayList<>();
        groupsList.add("quermesse_admin");

        UserRepresentation user = new UserRepresentation();
        user.setUsername(adminDto.getLogin());
        user.setFirstName(adminDto.getName());
        return createKeycloakUser(credential, groupsList, user);

    }

    public ResponseEntity<String> addUserEventAgent(String login){

        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(login);

        List<String> groupsList = new ArrayList<>();
        groupsList.add("agent_event");

        UserRepresentation user = new UserRepresentation();
        user.setUsername(login);
        return createKeycloakUser(credential, groupsList, user);

    }

    public ResponseEntity<String> addUserStandAgent(String login){

        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(login);

        List<String> groupsList = new ArrayList<>();
        groupsList.add("agent_stand");

        UserRepresentation user = new UserRepresentation();
        user.setUsername(login);
        return createKeycloakUser(credential, groupsList, user);

    }

    private ResponseEntity<String> createKeycloakUser(CredentialRepresentation credential, List<String> groupsList, UserRepresentation user) {
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        user.setGroups(groupsList);

        UsersResource instance = getInstance();
        try (Response response = instance.create(user)) {

            return ResponseEntity.status(response.getStatus()).body(response.getStatusInfo().toString());
        }
    }

    public List<UserRepresentation> getUser(String userName){
        UsersResource usersResource = getInstance();
        return usersResource.search(userName, true);

    }

    public void updateUserAdmin(AdminDto adminDto){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(adminDto.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(adminDto.getLogin());
        user.setFirstName(adminDto.getName());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        usersResource.get(adminDto.getAdminId()).update(user);
    }

    public void deleteUser(String userName){
        UsersResource usersResource = getInstance();
        List<UserRepresentation> users = usersResource.search(userName, true);
        if (users.isEmpty()){
            throw new RuntimeException("Não foi possível remover o usuário do sistema.");
        }
        UserRepresentation user = users.get(0);
        usersResource.get(user.getId())
                .remove();
    }


    public UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }


    public ResponseEntity<AgentKeycloakResponseDto> agentLogin(String username) {

        ResponseEntity<String> response = auth(username, username);

        AgentKeycloakResponseDto agentKeycloakResponseDto;
        try {
            if (response.getBody() == null){
                throw new BadCredentialsRuntimeException("Verifique o código do agente.");
            }
            agentKeycloakResponseDto = new ObjectMapper().readValue(
                    response.getBody().replace("not-before-policy", "not_before_policy"),
                    AgentKeycloakResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        agentKeycloakResponseDto.setNameNeed(false);
        if (agentKeycloakResponseDto.getScope().equals("AGENT_STAND")){
            var standAgentEntityOptional = standAgentRepository.findById(username);
            if (standAgentEntityOptional.isPresent()){
                if (standAgentEntityOptional.get().getStand() != null){
                    agentKeycloakResponseDto.setStandId(standAgentEntityOptional.get().getStand().getId());
                }
                if (standAgentEntityOptional.get().getName() == null){
                    agentKeycloakResponseDto.setNameNeed(true);
                }
            }
        } else if (agentKeycloakResponseDto.getScope().equals("AGENT_EVENT")){
            var eventAgentEntityOptional = eventAgentRepository.findById(username);
            if (eventAgentEntityOptional.isPresent()){
                if (eventAgentEntityOptional.get().getEvent() != null){
                    agentKeycloakResponseDto.setEventId(eventAgentEntityOptional.get().getEvent().getId());
                }
                if (eventAgentEntityOptional.get().getName() == null){
                    agentKeycloakResponseDto.setNameNeed(true);
                }
            }
        }

        return ResponseEntity.ok().body(agentKeycloakResponseDto);
    }

    public ResponseEntity<AdminKeycloakResponseDto> adminLogin(String login, String password) {

        ResponseEntity<String> response = auth(login, password);
        AdminKeycloakResponseDto adminKeycloakResponseDto;
        try {
            if (response.getBody() == null){
                throw new BadCredentialsRuntimeException("Verifique o login ou password!");
            }
            adminKeycloakResponseDto = new ObjectMapper().readValue(
                    response.getBody().replace("not-before-policy", "not_before_policy"),
                    AdminKeycloakResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (adminKeycloakResponseDto.getScope().equals("ADMIN")){
            var eventEntityOptional = eventRepository.findByLogin(login);
            if (eventEntityOptional.isPresent()){
                adminKeycloakResponseDto.setEventId(eventEntityOptional.get().getId());
            } else {
                throw new BadCredentialsRuntimeException("Verifique o evento! Este login não possui evento.");
            }
        }

        return ResponseEntity.ok().body(adminKeycloakResponseDto);
    }

    static ResponseEntity<String> auth(String login, String password) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID);
        map.add("username", login);
        map.add("password", password);
        map.add("grant_type", GRANT_TYPE);
        map.add("client_secret", CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForEntity(URL, request, String.class );
    }

}
