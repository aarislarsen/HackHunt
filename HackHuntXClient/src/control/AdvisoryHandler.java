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
public class AdvisoryHandler extends HackHuntClass
{
    private InputValidator inputValidator = new InputValidator();
    
    /**
     * default constructor
     */
    public AdvisoryHandler()
    {
        initProperties();
        configureLogLevels();
    }
    
    public String getSecondHint(String token, String advisoryID)
    {
        String response = "ERROR: ";
        if(inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            if(inputValidator.isValidInput(Constants.ALLOWEDININTS, advisoryID))
            {
                String[] interim = sendMessage("getSecondHint;"+token+";"+advisoryID).split(": ");
                if(interim.length > 1)
                {
                    response = decode(interim[1]);
                }      
                else
                {
                    log.log(DEBUG, "No hint available");
                    response = "";
                }
            }            
            else
            {   
                log.log(DEBUG,"Invalid character in advisory ID");
                response += "invalid character in advisory ID";
            }
        }
        else
        {
            log.log(DEBUG,"Invalid character in token");
            response += "invalid token";
        }
        return response;        
    }
    
    public String answerAdvisory(String token, String advisoryID, String answer)
    {
        String response = "ERROR: ";
        if(inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            if(inputValidator.isValidInput(Constants.ALLOWEDININTS, advisoryID))
            {
                if(inputValidator.isValidInput(Constants.ALLOWEDINSTRINGS, answer))
                {
                    response = sendMessage("answerAdvisory;"+token+";"+advisoryID+";"+answer);
                }
                else
                {
                    log.log(DEBUG,"Invalid character in answer");
                    response += "invalid character in answer";
                }                
            }            
            else
            {   
                log.log(DEBUG,"Invalid character in advisory ID");
                response += "invalid character in advisory ID";
            }
        }
        else
        {
            log.log(DEBUG,"Invalid character in token");
            response += "invalid token";
        }
        return response;  
    }
}
