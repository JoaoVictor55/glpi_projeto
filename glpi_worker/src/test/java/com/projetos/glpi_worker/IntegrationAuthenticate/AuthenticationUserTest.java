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
        var tokenResponse = authUser.authenticate(2);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";
    }

    @Test
    void testExperiationTime(){

        var tokenResponse = authUser.authenticate(2);

        System.out.printf("Expiration: %d\n", tokenResponse.expires_in());
        assert tokenResponse.expires_in() > 0 : "Token deveria experirar em um tempo válido";
    }

}
