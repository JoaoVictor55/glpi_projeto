package com.projetos.glpi_worker.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;

import com.projetos.glpi_worker.service.connection.AuthenticateUser;

@Component
public class AuthenticateCommand {

    @Autowired
    private AuthenticateUser auth;


    @Command(name = "authenticate", description = "Authenticate with GLPI", group = "GLPI",
            help = "Authenticate with GLPI using configured credentials")
    public void authenticate() {
        
        try{

            auth.authenticate();

            System.out.println("Authentication successful! Run 'token' command to see the access token.");
        }
        catch(Exception e) {
            System.out.println("Error to authenticate: " + e.getMessage());
        }
        
    }

    @Command(name = "token", description = "Get the current access token", group = "GLPI",
            help = "Retrieve the current access token")
    public String getToken() {
        try {
            return auth.getToken();
        } catch (Exception e) {
            return "Error retrieving token: " + e.getMessage(); 
        }
}
}
