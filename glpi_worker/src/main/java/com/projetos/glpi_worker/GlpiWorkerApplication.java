package com.projetos.glpi_worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GlpiWorkerApplication {
    public static void main(String[] args) {
        System.out.println("Oi");
        SpringApplication.run(GlpiWorkerApplication.class, args);
    }
}