package com.projetos.glpi_worker.IntegrationWireMockAuthenticate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.projetos.glpi_worker.domain.assets.Computer;
import com.projetos.glpi_worker.service.api_authentication.AuthenticateWithPassword;
import com.projetos.glpi_worker.service.api_authentication.GlpiConnectionProperties;
import com.projetos.glpi_worker.service.api_communication.ReadOnlyRequest;
import com.projetos.glpi_worker.service.api_communication.TimeoutGetRequest;

import io.netty.handler.timeout.TimeoutException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@WireMockTest(httpPort = 8081)
public class GetRequestTest {

    private AuthenticateWithPassword authUser;
    private TimeoutGetRequest timeoutGetRequest;

    @BeforeEach
    void setup() {
        
        GlpiConnectionProperties config = new GlpiConnectionProperties(

            "http://localhost:8081",  // URL do WireMock
            "test-client-id",
            "test-client-secret",
            "test-user",
            "test-pass",
            "test-scope",
            "/api.php"
        );
        
        timeoutGetRequest = new TimeoutGetRequest(config);
    }
    @Test
    void apiSlowResponseTimeout(){

        int limit = 3;
        int timeout = 15;

        stubFor(get(anyUrl())
        .willReturn(aResponse()
        .withStatus(200)
        .withFixedDelay(timeout*2)
        .withBody("[{\"id\":1}]")
        ));
        
        ReadOnlyRequest params = new ReadOnlyRequest(
            "/Assets/Computer", "VALID_TOKEN", timeout,
            null, null, Integer.toString(limit), 
            null);
                
        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, params);

        
    }

}
