package com.projetos.glpi_worker.constants;

public enum GlpiHeaderParams {


    GLPI_ENTITY("GLPI-Entity"),
    GLPI_PROFILE("GLPI-Profile"),
    GLPI_ENTITY_RECURSIVE("GLPI-Entity-Recursive"),

    AUTHORIZATION("Authorization"),
    BEARER("Bearer");

    private final String value;

    private GlpiHeaderParams(String value){

        this.value = value;
    }

    @Override
    public String toString(){

        return this.value;
    }

}
