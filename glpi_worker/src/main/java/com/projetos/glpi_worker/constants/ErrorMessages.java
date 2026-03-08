package com.projetos.glpi_worker.constants;

public enum ErrorMessages {


    TIME_OUT_REQUEST("API took too long to reply");

    private final String value;

    private ErrorMessages(String value){

        this.value = value;
    }

    @Override
    public String toString(){

        return this.value;
    }

}
