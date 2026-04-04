package com.projetos.glpi_worker.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import com.projetos.glpi_worker.service.api_authentication.AuthenticateWithPassword;

@Component
public class AuthenticateCommand {

   
    @Command(name="hello", description = "Say hello to a given name", group="Greetings",
        help = "A command that greets the user with a 'Hello ${name}!. Usage: hello [-n | --name]=<name>'"
    )
    public void sayHello(@Option(shortName = 'n', longName = "name", description = "the name of the person to greet", defaultValue = "World")
        String name){

            System.out.println("Hello "+name+"!");
            System.out.println("Everything seems in order!"); //interessante substituir por uma rotina de verificação básica


    }
}
