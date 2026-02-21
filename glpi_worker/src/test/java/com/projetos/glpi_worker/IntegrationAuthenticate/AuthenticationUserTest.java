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

    // Testa a autenticação básica, garantindo que um token válido seja gerado.
    @Test
    void toAuthenticate() {
        var tokenResponse = authUser.authenticate(2);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";
        System.out.println("Token gerado: " + tokenResponse.access_token());
    }

}
