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
public class PlayerHandler extends HackHuntClass
{
    private InputValidator inputValidator = new InputValidator();
    
    /**
     * constructor
     */
    public PlayerHandler()
    {
        initProperties();
        configureLogLevels();
    }
    
    /**
     * adds a new player
     * @param username
     * @param password
     * @param teamname
     * @return the raw return message from the server
     */
    public String addPlayer(String username, char[] password, String teamname)
    {
        String response = "ERROR: ";
        if(inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, username))
        {
            if(inputValidator.isValidInput(Constants.ALLOWEDINPASSWORD, new String(password)))
            {
                if(inputValidator.isValidInput(Constants.ALLOWEDINTEAMNAME, teamname))
                {
                    String message = "addUser;;username;password;teamname";
                    message = message.replace("username", username).replace("password", new String(password)).replace("teamname", teamname);
                    response = sendMessage(message);
                }
                else
                {
                    response = "ERROR: Invalid character in teamname";
                }
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
    
    /**
     * logs out the user
     * @param token
     * @return 
     */
    public String logoutPlayer(String token)
    {
        String response = "ERROR: ";        
        if(inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            String message = "logout;token";
            message = message.replace("token", token);
            response = sendMessage(message);
        }
        else
        {
            response = "ERROR: Invalid character in token";
        }                
        return response;
    }
}
