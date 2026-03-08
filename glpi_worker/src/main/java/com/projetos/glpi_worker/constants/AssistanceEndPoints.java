package com.projetos.glpi_worker.constants;


//constantes das URLs da API
public enum AssistanceEndPoints {

    ASSISTANCE("Assistance"),
    CHANGE("Change"),
    PROBLEM("Problem"),
    TICKET("Ticket");

    private final String name;

    AssistanceEndPoints(String name){

        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }


}
