package com.projetos.glpi_worker.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.projetos.glpi_worker.service.api_authentication.GlpiConnectionProperties;

import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

//melhor para injeção de independência :)
@Configuration
public class WebClientConfiguration {

    private GlpiConnectionProperties glpiConnectionProperties;

    @Autowired
    public WebClientConfiguration(GlpiConnectionProperties glpiConnectionProperties){

        this.glpiConnectionProperties = glpiConnectionProperties;

    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder){

         HttpClient httpClient = HttpClient.create()
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
                

        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(this.glpiConnectionProperties.url()+glpiConnectionProperties.apiEndpoint())
                .build();
        
        //.builder().baseUrl(this.glpiConnectionProperties.url()+glpiConnectionProperties.apiEndpoint()).build();
        
    }
}
