package com.projetos.glpi_worker;

import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.EnableCommand;
import org.springframework.stereotype.Component;


@Component
public class HelloWorld {

    @Command(name = "hello", description = "Say hello to a given name", group = "Greetings",
            help = "A command that greets the user with 'Hello ${name}!'. Usage: hello [-n | --name]=<name>")
    public String hello() {
        return "Olá, GLPI Worker operando!";
    }
}