package com.projetos.glpi_worker.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.projetos.glpi_worker.service.api_authentication.GlpiConnectionProperties;

//melhor para injeção de independência :)
@Configuration
public class WebClientConfiguration {

    private GlpiConnectionProperties glpiConnectionProperties;

    @Autowired
    public WebClientConfiguration(GlpiConnectionProperties glpiConnectionProperties){

        this.glpiConnectionProperties = glpiConnectionProperties;

    }

    @Bean
    public WebClient webClient(){

        return WebClient.builder().baseUrl(this.glpiConnectionProperties.url()+glpiConnectionProperties.apiEndpoint()).build();
        
    }
}
