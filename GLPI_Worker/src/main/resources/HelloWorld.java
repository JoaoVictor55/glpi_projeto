package com.projetos.glpi_worker;

@ShellComponent
public class HelloWorld {

    @ShellMethod(key = "hello", value = "Diz olá mundo")
    public String hello() {

        return "Hello world";
    }
}
