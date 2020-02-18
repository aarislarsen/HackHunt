/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import model.ScoreSystem;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Advisory;
import model.Protocol;
import util.Constants;
import util.InputValidator;

/**
 *
 * @author aarian
 */
public class AdvisoryHandler
{
    private final InputValidator inputValidator = new InputValidator();
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE;
    private Level WARNING = Level.WARNING;
    private Timer timer;
    private final String propertiesURL = "properties.properties";
    private Properties properties = new Properties();
    private ScoreSystem scoreHandler = new ScoreSystem();
    
    public AdvisoryHandler()
    {
        initProperties();
        configureLogLevels();
    }
    
    /**
     * gets the released advisories from the database
     * @param token the users authenticated token
     * @return a string representation of the released advisories minus the answer and the second hint     
     */
    public synchronized String getReleasedAdvisories(String token)
    {
        log.log(DEBUG,"Retrieving released advisories");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }  
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<Advisory> advisories = sql.getReleasedAdvisories();
            String returnString = "";
            if(advisories != null)
            {
                for(Advisory a : advisories)
                {
                    //grab each field and base64 it
                    String idEncoded = new Protocol().encode(""+a.getID());
                    String titleEncoded = new Protocol().encode(a.getTitle());                    
                    String difficultyEncoded = new Protocol().encode(a.getDifficulty());
                    String affectedServiceEncoded = new Protocol().encode(a.getAffectedService());
                    String descriptionEncoded = new Protocol().encode(a.getDescription());
                    String challengeEncoded = new Protocol().encode(a.getChallenge());
                    String challengeURIEncoded = new Protocol().encode(a.getChallengeURI());
                    String hintEncoded = new Protocol().encode(a.getHint());
                    //combine them with a ; as the delimiter and base64 that
                    String advisoryEncoded = new Protocol().encode(idEncoded+";"+titleEncoded+";"+difficultyEncoded+";"+affectedServiceEncoded+";"+descriptionEncoded+";"+challengeEncoded+";"+challengeURIEncoded+";"+hintEncoded);
                    //then add it to the returnString
                    returnString += advisoryEncoded+";";
                }
                return "SUCCESS: " + returnString;
            }
            else
            {
                return "ERROR: Released advisories could not be retrieved";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
    }
    
    /**
     * gets the answered advisories from the databased
     * @param token the users authenticated token
     * @return a string representation of the released advisories ID and timestamps
     */
    public synchronized String getAnsweredAdvisories(String token)
    {
        log.log(DEBUG,"Retrieving answered advisories");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }  
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<HashMap<Integer,String>> advisories = sql.getAnsweredAdvisories();
            String returnString = "";
            if(advisories != null)
            {
                for(HashMap h : advisories)
                {
                    String id = h.keySet().toArray()[0].toString();
                    returnString += id+","+h.get(Integer.parseInt(id))+";";
                }
                return "SUCCESS: " + returnString;
            }
            else
            {
                return "ERROR: Answered advisories could not be retrieved";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
    }    
    
    /**
     * Gets the ID, title, released and answered fields for all advisories
     * @return an arraylist with string arrays (4 size) with id, title, released, answered, in that order
     */
    public synchronized ArrayList<String[]> getAdvisoriesForOverview()
    {
        log.log(DEBUG,"Retrieving advisories for server-side overview");
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        ArrayList<String[]> advisories = sql.getAllAdvisoriesForOverview();
        return advisories;
    }
    
    /**
     * attempts to answer an advisory
     * @param token the users token, proving that he's logged in
     * @param advisoryID the advisory to attempt to answer
     * @param answer the proposed answer
     * @return SUCCESS and ID of correct, FAILURE and ID if wrong, ERROR if input isn't valid
     */
    public synchronized String answerAdvisory(String token, String advisoryID, String answer)
    {
        log.log(DEBUG,"Answering an advisory");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }  
        if(!inputValidator.isValidInput(Constants.ALLOWEDININTS, advisoryID) || advisoryID.isEmpty())
        {            
            log.log(DEBUG,"Invalid character in advisoryID");
            return "ERROR: Invalid characters in advisory ID, please try again";
        }
        if(!inputValidator.isValidInput(Constants.ALLOWEDINSTRINGS, answer))
        {
            System.out.println("hest");
            log.log(DEBUG,"Invalid character in answer");
            return "ERROR: Invalid characters in answer, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            int advisoryIDParsed = Integer.parseInt(advisoryID);
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            String username = sql.getActivePlayerByToken(token).get(0).get(0).toString();                   
            String advisoryAnswer = sql.getAdvisoryAnswer(advisoryIDParsed);
            if(advisoryAnswer != null) //check if advisory has been released
            {                
                //check if the user already answered this guestion correctly
                if(sql.didUserAlreadyAnswerThisAdvisoryCorrectly(username, advisoryIDParsed))
                {
                    log.log(DEBUG,"Advisory " + advisoryID +" has already been answered correctly by this user");
                    return "ERROR: Advisory " + advisoryID + " has already been answered correctly by you";
                }
                else
                {
                    if(advisoryAnswer.equals(answer)) //is the answer correct
                    {
                        //add answer to Answers and mark it as correct                    
                        sql.addAnswer(username, advisoryIDParsed, answer, ""+System.currentTimeMillis(), 1);
                        log.log(DEBUG,"Advisory answered correctly"); 
                        //add points (prestige) for answering the advisory
                        String difficulty = getAdvisory(advisoryIDParsed).getDifficulty();
                        //TODO
                        scoreHandler.addPrestige(difficulty, username);
                                
                        //check if the player is the first to answer it correctly
                        if(!sql.hasAdvisoryBeenAnswered(advisoryIDParsed))//TODO fire off webservice request or other notification?
                        {
                            //mark it as answered and with a timestamp. 
                            if(sql.markAdvisoryAsAnswered(advisoryIDParsed))
                            {
                                log.log(DEBUG,"Advisory answered for the first time");
                            }
                            else
                            {
                                log.log(DEBUG,"Advisory " + advisoryID +" could not be marked as answered");
                                return "ERROR: Advisory " + advisoryID + " could not be marked as answered";
                            }                        
                        }  
                        return "SUCCESS: "+advisoryID;
                    }
                    else
                    {
                        //add answer to Answers and mark it as wrong
                        sql.addAnswer(username, advisoryIDParsed, answer, ""+System.currentTimeMillis(), 0);
                        log.log(DEBUG,"Advisory answered incorrectly");
                        return "FAILURE: "+advisoryID;
                    }
                }
                
            }
            else
            {
                return "ERROR: Advisory could not be answered. Are you sure it's been released yet?";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
    }        
    
    /**
     * gets the advisory object for a given ID. WARNING: this advisory object also includes the correct answer!
     * @param advisoryID id of the advisory
     * @return advisory object, or null if the advisory id is invalid
     */
    public synchronized Advisory getAdvisory(int advisoryID)
    {
        if(!inputValidator.isValidInput(Constants.ALLOWEDININTS, ""+advisoryID) || (""+advisoryID).isEmpty())
        {            
            log.log(DEBUG,"Invalid character in advisoryID");
            return null;
        }
        else
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            return sql.getAdvisory(advisoryID);
        }
    }
    
    /**
     * checks to see if at least one token is present in the active players database
     * @param token
     * @return true if the token was found, false otherwise
     */
    private synchronized boolean isPlayerAuthenticated(String token)
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        ArrayList<ArrayList> foundTokens = sql.getActivePlayerByToken(token);
        boolean tokenInUse = false;
        for(ArrayList a : foundTokens)
        {
            if(a.get(1).toString().trim().equalsIgnoreCase(token))
            {                    
                log.log(DEBUG,"Users token found");
                tokenInUse = true;
                break;
            }
        }
        return tokenInUse;
    }
    
    /**
     * gets the released advisories from the database
     * @param token the users authenticated token
     * @return a string representation of the released advisories minus the answer and the second hint     
     */
    public synchronized String getUnansweredAdvisories(String token)
    {
        log.log(DEBUG,"Retrieving unanswered and released advisories");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }  
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<Advisory> advisories = sql.getUnansweredAdvisories(sql.getActivePlayerByToken(token).get(0).get(0).toString());
            String returnString = "";
            if(advisories != null)
            {
                for(Advisory a : advisories)
                {
                    //grab each field and base64 it
                    String idEncoded = new Protocol().encode(""+a.getID());
                    String titleEncoded = new Protocol().encode(a.getTitle());                    
                    String difficultyEncoded = new Protocol().encode(a.getDifficulty());
                    String affectedServiceEncoded = new Protocol().encode(a.getAffectedService());
                    String descriptionEncoded = new Protocol().encode(a.getDescription());
                    String challengeEncoded = new Protocol().encode(a.getChallenge());
                    String challengeURIEncoded = new Protocol().encode(a.getChallengeURI());
                    String hintEncoded = new Protocol().encode(a.getHint());
                    //combine them with a ; as the delimiter and base64 that
                    String advisoryEncoded = new Protocol().encode(idEncoded+";"+titleEncoded+";"+difficultyEncoded+";"+affectedServiceEncoded+";"+descriptionEncoded+";"+challengeEncoded+";"+challengeURIEncoded+";"+hintEncoded);
                    //then add it to the returnString
                    returnString += advisoryEncoded+";";
                }
                return "SUCCESS: " + returnString;
            }
            else
            {
                return "ERROR: Unanswered and released advisories could not be retrieved";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
    }
    
    /**
     * get the second hint of the given advisory
     * @param token the users token, proving that he's logged in
     * @param advisoryID the advisory to get the hint for     
     * @return SUCCESS and base64 encoded hint, ERROR if input isn't valid
     */
    public synchronized String getSecondHint(String token, String advisoryID)
    {
        log.log(DEBUG,"Getting second hint for advisory");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }  
        if(!inputValidator.isValidInput(Constants.ALLOWEDININTS, advisoryID) || advisoryID.isEmpty())
        {            
            log.log(DEBUG,"Invalid character in advisoryID");
            return "ERROR: Invalid characters in advisory ID, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            int advisoryIDParsed = Integer.parseInt(advisoryID);
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            String username = sql.getActivePlayerByToken(token).get(0).get(0).toString();                   
            String secondHint = sql.getSecondHint(advisoryIDParsed);
            sql.setCredit(sql.getCredit(username)-1, username);
            return "SUCCESS: " + new Protocol().encode(secondHint);
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
    }    
    
    /**
     * loads the properties from the file
     */
    private void initProperties()
    {
        try
        {
            properties.load(new FileInputStream(propertiesURL));
        }
        catch (IOException ex)
        {
            log.log(DEBUG,""+ex);
        }
    }
    
    /**
     * configures the LogLevel based on the properties file
     */
    private void configureLogLevels()
    {        
        String level = properties.getProperty("LogLevel");
        
        switch(level)
        {
            case "INFO":    INFO = Level.INFO; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.FINE;
                            break;
            case "DEBUG":   INFO = Level.INFO;
                            DEBUG = Level.INFO; 
                            WARNING = Level.INFO;
                            break;
            case "WARNING": INFO = Level.FINE; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.INFO;    
                            break;
            default:        INFO = Level.INFO; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.WARNING;
                            break;
        }
    }
    
    /**
     * updates an existing advisory
     * @param id
     * @param title
     * @param difficulty
     * @param service
     * @param description
     * @param challenge
     * @param challengeURI
     * @param hint
     * @param secondHint
     * @param answer
     * @param partOfVulnerability
     * @return SUCCESS and a message, or ERROR and a cause
     */
    public synchronized String updateAdvisory(String id, String title, String difficulty, String service, String description, String challenge, String challengeURI, String hint, String secondHint, String answer, String partOfVulnerability)
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        if(sql.getAdvisory(Integer.parseInt(id))!=null)
        {          
            if(id.length() >= 1 && title.length() > 1 && difficulty.length() > 1 && service.length() > 1 && description.length() > 1 && challenge.length() > 1 && hint.length() > 1 && answer.length() > 1 && partOfVulnerability.length() > 1)
            {
                if(sql.updateAdvisory(id, title, difficulty, service, description, challenge, challengeURI, hint, secondHint, answer, partOfVulnerability))
                {
                    return "SUCCESS: Advisory " + id + " was updated";
                }
                else
                {
                    return "ERROR: Advisory could not be updated";
                }
            }
            else
            {
                return "ERROR: All fields must be filled out";
            }
        }
        else
        {
            return "ERROR: Advisory could not be found";
        }
    }
    
    /**
     * Adds a new advisory to the database
     * @param title
     * @param difficulty
     * @param service
     * @param description
     * @param challenge
     * @param challengeURI
     * @param hint
     * @param secondHint
     * @param answer
     * @param partOfVulnerability
     * @return SUCCESS and a message, or ERROR and a cause
     */
    public synchronized String addAdvisory(String title, String difficulty, String service, String description, String challenge, String challengeURI, String hint, String secondHint, String answer, String partOfVulnerability)
    {
        if(title.length() >= 1 && difficulty.length() > 1 && service.length() > 1 && description.length() > 1 && challenge.length() > 1 && hint.length() > 1 && answer.length() > 1 && partOfVulnerability.length() > 1)
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            if(sql.addAdvisory(title, difficulty, service, description, challenge, challengeURI, hint, secondHint, answer, partOfVulnerability))
            {
                return "SUCCESS: Advisory " + title + " was created";
            }
            else
            {
                return "ERROR: Advisory could not be created";
            }
        }
        else
        {
            return "ERROR: All fields must be filled out";
        }
        
    }
}
