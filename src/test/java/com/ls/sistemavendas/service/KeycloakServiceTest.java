package com.ls.sistemavendas.service;

import com.ls.sistemavendas.config.KeycloakConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class KeycloakServiceTest {

    @MockBean
    private Keycloak keycloak;


    @BeforeEach
    public void setUp(){

    }

    @AfterEach
    public void tearDown(){

    }

    @Test
    void givenStaticMethodWithNoArgs_whenMocked_thenReturnsMockSuccessfully() {


        try (MockedStatic<KeycloakConfig> keycloakConfigMockedStatic = Mockito.mockStatic(KeycloakConfig.class)) {
            keycloakConfigMockedStatic.when(KeycloakConfig::getInstance).thenReturn(null);
            var instance = KeycloakConfig.getInstance();

            assertEquals(instance, null);
        }



    }
}
