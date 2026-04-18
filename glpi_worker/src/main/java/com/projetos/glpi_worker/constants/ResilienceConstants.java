package com.projetos.glpi_worker.constants;

public enum ResilienceConstants {


    RETRY_NAME_GLPI("glpiRetry"),
    CIRCUIT_BREAKER_NAME_GLPI("glpiCircuitBreaker");

    private final String name;

     ResilienceConstants(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }

}
