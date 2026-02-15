package com.projetos.glpi_worker.IntegrationAuthenticate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.projetos.glpi_worker.service.connection.AuthenticateWithPassword;

@SpringBootTest
public class AuthenticationUserTest {

    @Autowired
    private AuthenticateWithPassword authUser;

    @Test
    void testAutenticacao() {
        authUser.authenticate();
        assert authUser.getToken() != null && !authUser.getToken().isEmpty() : "Token de autenticação deve ser gerado";
    }

}
