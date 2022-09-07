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


    public ResponseEntity<String> addUser(AdminDto adminDto){

        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(adminDto.getPassword());

        List<String> groupsList = new ArrayList<>();
        groupsList.add("quermesse_admin");

        UserRepresentation user = new UserRepresentation();
        user.setUsername(adminDto.getLogin());
        user.setFirstName(adminDto.getName());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        user.setGroups(groupsList);

        UsersResource instance = getInstance();
        Response response = instance.create(user);

        return ResponseEntity.status(response.getStatus()).body(response.getStatusInfo().toString());

    }

    public List<UserRepresentation> getUser(String userName){
        UsersResource usersResource = getInstance();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;

    }

    public void updateUser(String userId, AdminDto adminDto){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(adminDto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(adminDto.getLogin());
        user.setFirstName(adminDto.getName());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(user);
    }


    public UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }

}
