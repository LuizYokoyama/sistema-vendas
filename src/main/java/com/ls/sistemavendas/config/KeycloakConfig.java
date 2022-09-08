package com.ls.sistemavendas.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakConfig {

    static Keycloak keycloak = null;
    final static String serverUrl = "http://localhost:8180/auth";
    public final static String realm = "quermesse";
    final static String clientId = "quermese_admin";
    final static String clientSecret = "zbji9pCixGxl1NByrdJJG3zYqJPL4mmN";
    final static String userName = "eventadmin";
    final static String password = "test";
    //final static String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJQNmpmZFFmRE9pcjEydzliRHUwdEV2UWtteEdOU1MxX0s2d05jdTZoOHg4In0.eyJleHAiOjE2NjI1OTI5ODAsImlhdCI6MTY2MjU3NDk4MCwianRpIjoiZmU0NTc3YWItOWVjNy00YjY2LWJlMjUtNzk5NjQxNWRjMGMzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL3F1ZXJtZXNzZSIsImF1ZCI6WyJyZWFsbS1tYW5hZ2VtZW50IiwiYWNjb3VudCJdLCJzdWIiOiI3YmUzNDY1OS01ZWY0LTRhNjktYTQ4YS0yNDU3N2NkNGJiYmMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJxdWVybWVzZV9hZG1pbiIsInNlc3Npb25fc3RhdGUiOiI1ZDYxMzIzMy02NTgxLTQ5ZjItYTdkYy1kYWFmZGY5ODExYzQiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkFETUlOIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LXJlYWxtIiwidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1hcHBsaWNhdGlvbnMiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsImRlbGV0ZS1hY2NvdW50Il19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiNWQ2MTMyMzMtNjU4MS00OWYyLWE3ZGMtZGFhZmRmOTgxMWM0IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoibm9tZSBkZSB0ZXN0ZSBhZG1pbiBhZG1pbiBkbyBldmVudG8iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJldmVudGFkbWluIiwiZ2l2ZW5fbmFtZSI6Im5vbWUgZGUgdGVzdGUgYWRtaW4iLCJmYW1pbHlfbmFtZSI6ImFkbWluIGRvIGV2ZW50byJ9.N_elS_Dxuz3iu44z7K8g5jgYHv-6gzjNJ7tVG7I40Cz2_sz0u_7EnfXSj7PwPxCztL3Dq_iBbf5mhRTLLwerfpVqKyc479373NCrtQGsSOb7SeQ0sgsbqYxn-ZUwVUYXkUcf79Ht65DA-1cYiQtsZU3FkibUufVew39nuG_84Hqnxz2QFO7qbQ7CQE3KRmjdnJrZqINpou4Yjlb-KLhd8bQ4xKk65ZlBBkBYJx7IuULLcNfa46QjofFFw7d1IFSa6mjFJ1y1WTXqi7CfHvtIAu92mb8WHnWU8RenxxZGjRoR8K2I0Yso1OuD7k6NAL1SHQPnjYbwew3aq_XM4jRTGw";


    public KeycloakConfig() {
    }

    public static Keycloak getInstance(){
        if(keycloak == null){

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    //.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    //.authorization(token)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build()
                    )
                    .build();
        }
        return keycloak;
    }
}
