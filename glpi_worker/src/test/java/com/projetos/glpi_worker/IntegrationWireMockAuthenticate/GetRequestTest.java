package com.projetos.glpi_worker.IntegrationWireMockAuthenticate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.projetos.glpi_worker.domain.assets.Computer;
import com.projetos.glpi_worker.service.api_authentication.GlpiConnectionProperties;
import com.projetos.glpi_worker.service.api_communication.ReadOnlyRequest;
import com.projetos.glpi_worker.service.api_communication.TimeoutRequestMaker;


import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@WireMockTest(httpPort = 8081)
public class GetRequestTest {


    private TimeoutRequestMaker timeoutGetRequest;

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
        
        WebClient webClient = WebClient.builder().baseUrl(config.url()+config.apiEndpoint()).build();
        timeoutGetRequest = new TimeoutRequestMaker(webClient);
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
                
        Flux<Computer> response = timeoutGetRequest.getRequest(Computer.class,params.url(),params.token(),params.timeout(),null, null, null, timeout, null, null);

        StepVerifier.create(response)
        .expectError(RuntimeException.class)
        .verify();

        
    }

}
