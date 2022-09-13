package com.ls.sistemavendas.service;

import com.ls.sistemavendas.config.Credentials;
import com.ls.sistemavendas.config.KeycloakConfig;
import com.ls.sistemavendas.dto.AdminDto;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class KeyCloakService {


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

        UsersResource usersResource = getInstance();
        List<UserRepresentation> users = usersResource.search(adminDto.getLogin(), true);
        if (users.isEmpty()){
            throw new RuntimeException("Não foi possível atualizar este Login");
        }

        UserRepresentation user = users.get(0);
        user.setUsername(adminDto.getLogin());
        user.setFirstName(adminDto.getName());
        user.setCredentials(Collections.singletonList(credential));

        usersResource.get(user.getId()).update(user);
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

}
