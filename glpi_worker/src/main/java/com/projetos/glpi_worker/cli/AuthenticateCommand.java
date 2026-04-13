package com.projetos.glpi_worker.cli;
import java.util.Arrays;

import org.springframework.shell.core.command.CommandContext;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

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

    @Command(name="login", description = "Log into glpi with credentials", group="Authentication",
        help = "A command that allows you to log in into application. Usage: login [-f | --file]=<path_file_with_login_info> [-p | --password]=<yes | no>"
    )
    public void login(@Option(shortName = 'f', longName = "file", description = "the file that contains the login information", defaultValue = "") String file,
    @Option(shortName = 'p', longName = "password", description = "pass the user\'s name and password directly in shell", defaultValue = "false") boolean password, CommandContext context){

        if(password){

            String username;
            String passwordUser;
            try{
                
                username = context.inputReader().readInput("Enter the username: ");
                passwordUser = Arrays.toString(context.inputReader().readPassword("Enter password: "));
            }
            catch(Exception e){

                return;
            }

            System.out.println("Username: "+username);
            System.out.println("Password: "+passwordUser);
           


        }

    }
}
