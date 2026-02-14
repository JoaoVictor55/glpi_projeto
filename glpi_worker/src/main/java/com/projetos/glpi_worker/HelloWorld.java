package com.projetos.glpi_worker;

import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.EnableCommand;
import org.springframework.stereotype.Component;
import com.projetos.glpi_worker.service.connection.AuthenticateUser;

@Component
public class HelloWorld {

    @Command(name = "hello", description = "Say hello to a given name", group = "Greetings",
            help = "A command that greets the user with 'Hello ${name}!'. Usage: hello [-n | --name]=<name>")
    public String hello() {
        return "Olá, GLPI Worker operando!";
    }

    @Command(name = "conectar", description = "Say hello to a given name", group = "Greetings",
            help = "A command that greets the user with 'Hello ${name}!'. Usage: hello-name [-n | --name]=<name>")
    public void conectar() {


        var CLIENT_ID="2bbeb091700f3d9925de13ad128b53ee5ab7c1ba5e237ca705d2ef593f0b653d";
        var CLIENT_SECRET="13e4c75d75c182e4beca893b77956994b3df93f319f1386db0424b8f4abd93bd";
        var USERNAME="API_usuario";
        var PASSWORD="1";
        var SCOPE="status";

        AuthenticateUser auth = new AuthenticateUser(
            "http://localhost",
            CLIENT_ID,
            CLIENT_SECRET,
            USERNAME,
            PASSWORD,
            SCOPE
        );
        auth.authenticate();
}
}