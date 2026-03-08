package com.projetos.glpi_worker.constants;

public enum GlpiQueryParams {
    
    FILTER("filter"), START("start"), LIMIT("limit");

    private final String value;

    private GlpiQueryParams(String value){

        this.value = value;
    }

    @Override
    public String toString(){

        return this.value;
    }
}
