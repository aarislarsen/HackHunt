/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import model.HackHuntClass;
import util.Constants;
import util.InputValidator;

/**
 *
 * @author Dragon
 */
public class LoginHandler extends HackHuntClass
{
    private InputValidator inputValidator = new InputValidator();
    
    public LoginHandler()
    {
        initProperties();
        configureLogLevels();
    }
    
    /**
     * logs in
     * @param username
     * @param password
     * @return the raw return message from the server 
     */
    public String login(String username, char[] password)
    {
        String response = "ERROR: ";
        if(inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, username))
        {
            if(inputValidator.isValidInput(Constants.ALLOWEDINPASSWORD, new String(password)))
            {                
                String message = "login;;username;password";
                message = message.replace("username", username).replace("password", new String(password));
                response = sendMessage(message);                
            }
            else
            {
                response = "ERROR: Invalid character in password";
            }
        }
        else
        {
            response = "ERROR: Invalid character in username";
        }                
        return response;
    }
    
}
