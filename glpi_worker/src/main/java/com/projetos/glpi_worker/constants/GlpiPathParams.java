package com.projetos.glpi_worker.constants;

public enum GlpiPathParams {

    ID("id"),
    SUBITEM_ID("subitem_id");
    
    private final String value;

    private GlpiPathParams(String value){

        this.value = value;

    }

    @Override
    public String toString(){

        return this.value;
    }
}
